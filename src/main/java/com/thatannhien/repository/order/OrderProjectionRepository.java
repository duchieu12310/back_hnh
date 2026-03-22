package com.thatannhien.repository.order;

import com.thatannhien.dto.statistic.ProductSalesStatistic;
import com.thatannhien.dto.statistic.StatisticResource;
import com.thatannhien.entity.order.Order;
import com.thatannhien.entity.order.OrderVariant;
import com.thatannhien.entity.product.Product;
import com.thatannhien.entity.product.Variant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@AllArgsConstructor
public class OrderProjectionRepository {

    private EntityManager em;

    public List<StatisticResource> getOrderCountByCreateDate() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<StatisticResource> query = cb.createQuery(StatisticResource.class);

        Root<Order> order = query.from(Order.class);
        query.select(cb.construct(StatisticResource.class, order.get("createdAt").as(Instant.class), cb.count(order.get("id"))));
        query.groupBy(order.get("createdAt").as(Instant.class));
        query.orderBy(cb.asc(order.get("createdAt")));

        return em.createQuery(query).getResultList();
    }

    /**
     * Tính doanh thu theo ngày từ các đơn hàng đã giao thành công (status = 4) và đã thanh toán (paymentStatus = 2)
     */
    public List<StatisticResource> getRevenueByCreateDate() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Order> order = query.from(Order.class);
        
        // Lọc đơn hàng đã giao thành công và đã thanh toán
        Predicate statusPredicate = cb.equal(order.get("status"), 4);
        Predicate paymentStatusPredicate = cb.equal(order.get("paymentStatus"), 2);
        Predicate wherePredicate = cb.and(statusPredicate, paymentStatusPredicate);
        
        // Tính tổng doanh thu (totalPay)
        query.select(cb.array(
            order.get("createdAt").as(Instant.class),
            cb.sum(order.get("totalPay"))
        ));
        query.where(wherePredicate);
        query.groupBy(order.get("createdAt").as(Instant.class));
        query.orderBy(cb.asc(order.get("createdAt")));

        // Map kết quả từ Object[] sang StatisticResource
        List<Object[]> results = em.createQuery(query).getResultList();
        return results.stream()
            .map(row -> {
                Instant date = (Instant) row[0];
                BigDecimal totalPay = (BigDecimal) row[1];
                return new StatisticResource(date, totalPay.longValue());
            })
            .collect(java.util.stream.Collectors.toList());
    }

    /**
     * Lấy danh sách sản phẩm bán chạy trong tháng (30 ngày gần nhất)
     * Chỉ tính các đơn hàng đã giao thành công (status = 4) và đã thanh toán (paymentStatus = 2)
     */
    public List<ProductSalesStatistic> getTopSellingProductsThisMonth(int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<OrderVariant> orderVariant = query.from(OrderVariant.class);
        Join<OrderVariant, Order> order = orderVariant.join("order");
        Join<OrderVariant, Variant> variant = orderVariant.join("variant");
        Join<Variant, Product> product = variant.join("product");

        // Lọc đơn hàng đã giao thành công và đã thanh toán
        Predicate statusPredicate = cb.equal(order.get("status"), 4);
        Predicate paymentStatusPredicate = cb.equal(order.get("paymentStatus"), 2);
        
        // Lọc theo tháng (30 ngày gần nhất)
        Instant now = Instant.now();
        Instant monthAgo = now.minus(30, ChronoUnit.DAYS);
        Predicate datePredicate = cb.greaterThanOrEqualTo(order.get("createdAt").as(Instant.class), monthAgo);
        
        Predicate wherePredicate = cb.and(statusPredicate, paymentStatusPredicate, datePredicate);

        // Group by Product và tính tổng quantity và revenue
        query.select(cb.array(
            product.get("id"),
            product.get("name"),
            product.get("code"),
            cb.sum(orderVariant.get("quantity")),
            cb.sum(orderVariant.get("amount"))
        ));
        query.where(wherePredicate);
        query.groupBy(
            product.get("id"),
            product.get("name"),
            product.get("code")
        );
        query.orderBy(cb.desc(cb.sum(orderVariant.get("quantity"))));

        List<Object[]> results = em.createQuery(query)
            .setMaxResults(limit)
            .getResultList();

        return results.stream()
            .map(row -> {
                Long productId = ((Number) row[0]).longValue();
                String productName = (String) row[1];
                String productCode = (String) row[2];
                Long totalQuantity = ((Number) row[3]).longValue();
                BigDecimal totalRevenue = (BigDecimal) row[4];
                return new ProductSalesStatistic(
                    productId,
                    productName,
                    productCode,
                    totalQuantity,
                    totalRevenue.longValue()
                );
            })
            .collect(Collectors.toList());
    }

    /**
     * Lấy danh sách sản phẩm không bán được trong tháng (30 ngày gần nhất)
     * Lấy các sản phẩm không có đơn hàng nào đã giao thành công và đã thanh toán trong 30 ngày gần nhất
     */
    public List<ProductSalesStatistic> getSlowSellingProductsThisMonth(int limit) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<Product> product = query.from(Product.class);
        
        // Subquery để kiểm tra xem sản phẩm có bán trong tháng không
        javax.persistence.criteria.Subquery<Long> soldProductsSubquery = query.subquery(Long.class);
        Root<OrderVariant> orderVariantSq = soldProductsSubquery.from(OrderVariant.class);
        Join<OrderVariant, Order> orderSq = orderVariantSq.join("order");
        Join<OrderVariant, Variant> variantSq = orderVariantSq.join("variant");
        
        // Điều kiện: đơn hàng đã giao thành công, đã thanh toán và trong 30 ngày gần nhất
        Instant now = Instant.now();
        Instant monthAgo = now.minus(30, ChronoUnit.DAYS);
        
        soldProductsSubquery.select(cb.literal(1L));
        soldProductsSubquery.where(cb.and(
            cb.equal(variantSq.get("product").get("id"), product.get("id")), // Liên kết với product bên ngoài
            cb.equal(orderSq.get("status"), 4), // Đã giao thành công
            cb.equal(orderSq.get("paymentStatus"), 2), // Đã thanh toán
            cb.greaterThanOrEqualTo(orderSq.get("createdAt").as(Instant.class), monthAgo) // Trong 30 ngày gần nhất
        ));
        
        // Lấy các sản phẩm KHÔNG có bán trong tháng (NOT EXISTS)
        query.select(cb.array(
            product.get("id"),
            product.get("name"),
            product.get("code"),
            cb.literal(0L), // Số lượng bán = 0
            cb.literal(BigDecimal.ZERO) // Doanh thu = 0
        ));
        query.where(cb.not(cb.exists(soldProductsSubquery)));
        query.orderBy(cb.asc(product.get("id")));

        List<Object[]> results = em.createQuery(query)
            .setMaxResults(limit)
            .getResultList();

        return results.stream()
            .map(row -> {
                Long productId = ((Number) row[0]).longValue();
                String productName = (String) row[1];
                String productCode = (String) row[2];
                return new ProductSalesStatistic(
                    productId,
                    productName,
                    productCode,
                    0L, // Không bán được nên số lượng = 0
                    0L  // Không bán được nên doanh thu = 0
                );
            })
            .collect(Collectors.toList());
    }

}
