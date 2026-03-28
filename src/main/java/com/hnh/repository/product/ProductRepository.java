package com.hnh.repository.product;

import com.hnh.constant.SearchFields;
import com.hnh.entity.inventory.Docket;
import com.hnh.entity.inventory.DocketVariant;
import com.hnh.entity.order.Order;
import com.hnh.entity.order.OrderVariant;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Variant;
import com.hnh.utils.SearchUtils;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import io.github.perplexhub.rsql.RSQLCustomPredicate;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    default Page<Product> findDocketedProducts(Pageable pageable) {
        Specification<Product> spec = (root, query, cb) -> {
            Join<Product, Variant> variant = root.join("variants");
            Join<Variant, DocketVariant> docketVariant = variant.join("docketVariants");

            query.distinct(true);
            query.orderBy(cb.desc(docketVariant.get("docket").get("id")));

            return query.getRestriction();
        };

        return findAll(spec, pageable);
    }

    default Page<Product> findByParams(String filter,
                                       String sort,
                                       String search,
                                       boolean saleable,
                                       boolean newable,
                                       boolean slowSelling,
                                       Pageable pageable) {
        // Xử lý `filter` thành Specification
        RSQLCustomPredicate<String> jsonPredicate = new RSQLCustomPredicate<>(
                new ComparisonOperator("=json=", true),
                String.class,
                input -> {
                    CriteriaBuilder cb = input.getCriteriaBuilder();

                    // Lấy phần còn lại của danh sách `input.getArguments()` sau khi bỏ qua phần tử đầu tiên
                    Object[] values = input.getArguments().stream().skip(1).toArray();

                    return cb.function("JSON_EXTRACT", String.class,
                            input.getPath(),
                            cb.function("REPLACE", String.class,
                                    cb.function("JSON_UNQUOTE", String.class,
                                            cb.function("JSON_SEARCH", String.class,
                                                    input.getPath(),
                                                    cb.literal("one"),
                                                    cb.literal(input.getArguments().get(0)))
                                    ),
                                    cb.literal(".code"),
                                    cb.literal(".value")
                            )
                    ).in(values);
                });

        Specification<Product> filterable = RSQLJPASupport.toSpecification(filter, List.of(jsonPredicate));
        Specification<Product> searchable = SearchUtils.parse(search, SearchFields.CLIENT_PRODUCT);

        // Lọc theo `saleable` (có thể bán) và `newable` (thứ tự mới nhất)
        Specification<Product> docketable = (root, query, cb) -> {
            List<Predicate> wheres = new ArrayList<>();
            List<javax.persistence.criteria.Order> orders = new ArrayList<>();

            Join<Product, Variant> variant = root.join("variants", javax.persistence.criteria.JoinType.LEFT);
            Join<Variant, DocketVariant> docketVariant = variant.join("docketVariants", javax.persistence.criteria.JoinType.LEFT);
            Join<DocketVariant, Docket> docket = docketVariant.join("docket", javax.persistence.criteria.JoinType.LEFT);

            if (saleable) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<Variant> variantSq = subquery.from(Variant.class);
                Join<Variant, DocketVariant> docketVariantSq = variantSq.join("docketVariants");
                Join<DocketVariant, Docket> docketSq = docketVariantSq.join("docket");

                subquery.select(cb.diff(
                        cb.sum(
                                cb.<Integer>selectCase()
                                        .when(cb.and(cb.equal(docketSq.get("type"), 1),
                                                        cb.equal(docketSq.get("status"), 3)),
                                                docketVariantSq.get("quantity"))
                                        .when(cb.and(cb.equal(docketSq.get("type"), 2),
                                                        cb.equal(docketSq.get("status"), 3)),
                                                cb.prod(docketVariantSq.get("quantity"), -1))
                                        .otherwise(0)
                        ),
                        cb.sum(
                                cb.<Integer>selectCase()
                                        .when(cb.and(cb.equal(docketSq.get("type"), 2),
                                                        docketSq.get("status").in(1, 2)),
                                                docketVariantSq.get("quantity"))
                                        .otherwise(0)
                        )
                ));

                subquery.where(cb.equal(variantSq.get("product").get("id"), root.get("id")));
                subquery.groupBy(variantSq.get("product").get("id"));

                wheres.add(cb.greaterThan(subquery, 0));
            }

            if ("lowest-price".equals(sort)) {
                orders.add(cb.asc(cb.min(variant.get("price"))));
            }

            if ("highest-price".equals(sort)) {
                orders.add(cb.desc(cb.max(variant.get("price"))));
            }

            if ("random".equals(sort)) {
                orders.add(cb.asc(cb.function("RAND", Void.class)));
            }

            if (newable) {
                wheres.add(cb.equal(docket.get("type"), 1));
                wheres.add(cb.equal(docket.get("status"), 3));

                orders.add(cb.desc(cb.max(docket.get("createdAt"))));
                orders.add(cb.asc(root.get("id")));
            }

            if (slowSelling) {
                // Tính tổng số lượng bán từ OrderVariant trong các đơn hàng đã hoàn thành (status = 4)
                Subquery<Integer> salesSubquery = query.subquery(Integer.class);
                Root<Variant> variantSalesSq = salesSubquery.from(Variant.class);
                Join<Variant, OrderVariant> orderVariantSq = variantSalesSq.join("orderVariants");
                Join<OrderVariant, Order> orderSq = orderVariantSq.join("order");

                salesSubquery.select(cb.coalesce(cb.sum(orderVariantSq.get("quantity")), 0));
                salesSubquery.where(
                    cb.and(
                        cb.equal(variantSalesSq.get("product").get("id"), root.get("id")),
                        cb.equal(orderSq.get("status"), 4) // Đơn hàng đã giao thành công
                    )
                );
                salesSubquery.groupBy(variantSalesSq.get("product").get("id"));

                // Chỉ lấy sản phẩm đã có bán (số lượng bán > 0)
                wheres.add(cb.greaterThan(salesSubquery, 0));

                // Sắp xếp: sử dụng cách tiếp cận khác vì không thể dùng subquery trong ORDER BY với GROUP BY
                // Sẽ sort sau khi lấy dữ liệu
            }

            Optional.ofNullable(filterable.toPredicate(root, query, cb)).ifPresent(wheres::add);
            Optional.ofNullable(searchable.toPredicate(root, query, cb)).ifPresent(wheres::add);

            query.where(wheres.toArray(Predicate[]::new));
            query.groupBy(root.get("id"));
            query.orderBy(orders);

            return query.getRestriction();
        };

        /*
         * Bug:
         * https://stackoverflow.com/a/59046245
         * https://stackoverflow.com/a/37771947
         *
         * TODO: Cần tìm cách hiệu quả hơn (sử dụng EntityManager)
         */
        List<Product> products = findAll(docketable);
        
        // Nếu slowSelling = true, sort theo số lượng bán giảm dần
        if (slowSelling) {
            List<Long> productIds = products.stream().map(Product::getId).toList();
            List<Object[]> salesData = findTotalSalesByProductIds(productIds);
            
            // Tạo map productId -> totalSales
            java.util.Map<Long, Integer> salesMap = new java.util.HashMap<>();
            for (Object[] row : salesData) {
                Long productId = ((Number) row[0]).longValue();
                Integer totalSales = ((Number) row[1]).intValue();
                salesMap.put(productId, totalSales);
            }
            
            // Sort products theo số lượng bán giảm dần
            products.sort((p1, p2) -> {
                Integer sales1 = salesMap.getOrDefault(p1.getId(), 0);
                Integer sales2 = salesMap.getOrDefault(p2.getId(), 0);
                int compare = sales2.compareTo(sales1); // Giảm dần
                if (compare == 0) {
                    return p1.getId().compareTo(p2.getId()); // Nếu bằng nhau, sort theo ID
                }
                return compare;
            });
        }
        
        final int start = (int) pageable.getOffset();
        final int end = Math.min(start + pageable.getPageSize(), products.size());

        return new PageImpl<>(products.subList(start, end), pageable, products.size());
    }

    Optional<Product> findBySlug(String slug);


    @Query("SELECT COUNT(p.id) FROM Product p")
    int countByProductId();

    @Query("SELECT v.product.id, COALESCE(SUM(ov.quantity), 0) " +
           "FROM Variant v " +
           "INNER JOIN v.orderVariants ov " +
           "INNER JOIN ov.order o " +
           "WHERE v.product.id IN :productIds AND o.status = 4 " +
           "GROUP BY v.product.id")
    List<Object[]> findTotalSalesByProductIds(List<Long> productIds);

}

