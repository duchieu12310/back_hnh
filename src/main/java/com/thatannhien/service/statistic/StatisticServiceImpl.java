package com.thatannhien.service.statistic;

import com.thatannhien.dto.statistic.ProductSalesStatistic;
import com.thatannhien.dto.statistic.StatisticResource;
import com.thatannhien.dto.statistic.StatisticResponse;
import com.thatannhien.repository.authentication.UserProjectionRepository;
import com.thatannhien.repository.customer.CustomerRepository;
import com.thatannhien.repository.order.OrderProjectionRepository;
import com.thatannhien.repository.order.OrderRepository;
import com.thatannhien.repository.product.BrandRepository;
import com.thatannhien.repository.product.ProductRepository;
import com.thatannhien.repository.product.SupplierRepository;
import com.thatannhien.repository.promotion.PromotionRepository;
import com.thatannhien.repository.review.ReviewProjectionRepository;
import com.thatannhien.repository.review.ReviewRepository;
import com.thatannhien.repository.waybill.WaybillProjectionRepository;
import com.thatannhien.repository.waybill.WaybillRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class StatisticServiceImpl implements StatisticService {

    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private WaybillRepository waybillRepository;
    private PromotionRepository promotionRepository;
    private SupplierRepository supplierRepository;
    private BrandRepository brandRepository;
    private ReviewRepository reviewRepository;
    private UserProjectionRepository userProjectionRepository;
    private OrderProjectionRepository orderProjectionRepository;
    private WaybillProjectionRepository waybillProjectionRepository;
    private ReviewProjectionRepository reviewProjectionRepository;

    @Override
    public StatisticResponse getStatistic() {
        StatisticResponse statisticResponse = new StatisticResponse();

        // TODO: Nên dùng tên hàm `count` hợp lý hơn, như `countAll()`
        int totalCustomer = customerRepository.countByCustomerId();
        int totalProduct = productRepository.countByProductId();
        int totalOrder = orderRepository.countByOrderId();
        int totalWaybill = waybillRepository.countByWaybillId();
        int totalReview = reviewRepository.countByReviewId();
        int totalActivePromotion = promotionRepository.countByPromotionId();
        int totalSupplier = supplierRepository.countBySupplierId();
        int totalBrand = brandRepository.countByBrandId();

        List<StatisticResource> statisticRegistration = userProjectionRepository.getUserCountByCreateDate();
        List<StatisticResource> statisticOrder = orderProjectionRepository.getOrderCountByCreateDate();
        List<StatisticResource> statisticReview = reviewProjectionRepository.getReviewCountByCreateDate();
        List<StatisticResource> statisticWaybill = waybillProjectionRepository.getWaybillCountByCreateDate();
        List<StatisticResource> statisticRevenue = orderProjectionRepository.getRevenueByCreateDate();

        statisticResponse.setTotalCustomer(totalCustomer);
        statisticResponse.setTotalProduct(totalProduct);
        statisticResponse.setTotalOrder(totalOrder);
        statisticResponse.setTotalWaybill(totalWaybill);
        statisticResponse.setTotalReview(totalReview);
        statisticResponse.setTotalActivePromotion(totalActivePromotion);
        statisticResponse.setTotalSupplier(totalSupplier);
        statisticResponse.setTotalBrand(totalBrand);
        statisticResponse.setStatisticRegistration(statisticRegistration);
        statisticResponse.setStatisticOrder(statisticOrder);
        statisticResponse.setStatisticReview(statisticReview);
        statisticResponse.setStatisticWaybill(statisticWaybill);
        statisticResponse.setStatisticRevenue(statisticRevenue);

        // Lấy top 10 sản phẩm bán chạy và bán chậm trong tháng
        List<ProductSalesStatistic> topSellingProducts = orderProjectionRepository.getTopSellingProductsThisMonth(10);
        List<ProductSalesStatistic> slowSellingProducts = orderProjectionRepository.getSlowSellingProductsThisMonth(10);
        statisticResponse.setTopSellingProducts(topSellingProducts);
        statisticResponse.setSlowSellingProducts(slowSellingProducts);

        return statisticResponse;
    }

}
