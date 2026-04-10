package com.hnh.controller;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.address.*;
import com.hnh.dto.authentication.*;
import com.hnh.dto.cashbook.*;
import com.hnh.dto.chat.*;
import com.hnh.dto.general.*;
import com.hnh.dto.order.*;
import com.hnh.dto.product.*;
import com.hnh.dto.promotion.*;
import com.hnh.dto.review.*;
import com.hnh.dto.reward.*;
import com.hnh.dto.waybill.*;
import com.hnh.entity.address.*;
import com.hnh.entity.authentication.*;
import com.hnh.entity.cashbook.*;
import com.hnh.entity.chat.*;
import com.hnh.entity.general.*;
import com.hnh.entity.order.*;
import com.hnh.entity.product.*;
import com.hnh.entity.reward.*;
import com.hnh.mapper.address.*;
import com.hnh.mapper.authentication.*;
import com.hnh.mapper.cashbook.*;
import com.hnh.mapper.chat.*;
import com.hnh.mapper.general.*;
import com.hnh.mapper.order.*;
import com.hnh.mapper.product.*;
import com.hnh.mapper.reward.*;
import com.hnh.repository.address.*;
import com.hnh.repository.authentication.*;
import com.hnh.repository.cashbook.*;
import com.hnh.repository.chat.*;
import com.hnh.repository.general.*;
import com.hnh.repository.order.*;
import com.hnh.repository.product.*;
import com.hnh.repository.reward.*;
import com.hnh.service.*;
import com.hnh.service.address.ProvinceService;
import com.hnh.service.promotion.PromotionService;
import com.hnh.service.review.ReviewService;
import com.hnh.service.waybill.WaybillService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class GenericMappingRegister {

    @Autowired
    private ApplicationContext context;
    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    // Controllers
    @Autowired private GenericController<ProvinceRequest, ProvinceResponse> provinceController;
    @Autowired private GenericController<DistrictRequest, DistrictResponse> districtController;
    @Autowired private GenericController<WardRequest, WardResponse> wardController;
    @Autowired private GenericController<AddressRequest, AddressResponse> addressController;
    @Autowired private GenericController<UserRequest, UserResponse> userController;
    @Autowired private GenericController<RoleRequest, RoleResponse> roleController;
    @Autowired private GenericController<PropertyRequest, PropertyResponse> propertyController;
    @Autowired private GenericController<CategoryRequest, CategoryResponse> categoryController;
    @Autowired private GenericController<TagRequest, TagResponse> tagController;
    @Autowired private GenericController<GuaranteeRequest, GuaranteeResponse> guaranteeController;
    @Autowired private GenericController<UnitRequest, UnitResponse> unitController;
    @Autowired private GenericController<SupplierRequest, SupplierResponse> supplierController;
    @Autowired private GenericController<BrandRequest, BrandResponse> brandController;
    @Autowired private GenericController<SpecificationRequest, SpecificationResponse> specificationController;
    @Autowired private GenericController<ProductRequest, ProductResponse> productController;
    @Autowired private GenericController<VariantRequest, VariantResponse> variantController;
    @Autowired private GenericController<ImageRequest, ImageResponse> imageController;
    @Autowired private GenericController<OrderResourceRequest, OrderResourceResponse> orderResourceController;
    @Autowired private GenericController<OrderCancellationReasonRequest, OrderCancellationReasonResponse> orderCancellationReasonController;
    @Autowired private GenericController<OrderRequest, OrderResponse> orderController;
    @Autowired private GenericController<WaybillRequest, WaybillResponse> waybillController;
    @Autowired private GenericController<ReviewRequest, ReviewResponse> reviewController;
    @Autowired private GenericController<PaymentMethodRequest, PaymentMethodResponse> paymentMethodController;
    @Autowired private GenericController<PromotionRequest, PromotionResponse> promotionController;
    @Autowired private GenericController<RoomRequest, RoomResponse> roomController;
    @Autowired private GenericController<RewardStrategyRequest, RewardStrategyResponse> rewardStrategyController;

    // Services
    @Autowired private ProvinceService provinceService;
    @Autowired private GenericService<District, DistrictRequest, DistrictResponse> districtService;
    @Autowired private GenericService<Ward, WardRequest, WardResponse> wardService;
    @Autowired private GenericService<Address, AddressRequest, AddressResponse> addressService;
    @Autowired private GenericService<User, UserRequest, UserResponse> userService;
    @Autowired private GenericService<Role, RoleRequest, RoleResponse> roleService;
    @Autowired private GenericService<Property, PropertyRequest, PropertyResponse> propertyService;
    @Autowired private GenericService<Category, CategoryRequest, CategoryResponse> categoryService;
    @Autowired private GenericService<Tag, TagRequest, TagResponse> tagService;
    @Autowired private GenericService<Guarantee, GuaranteeRequest, GuaranteeResponse> guaranteeService;
    @Autowired private GenericService<Unit, UnitRequest, UnitResponse> unitService;
    @Autowired private GenericService<Supplier, SupplierRequest, SupplierResponse> supplierService;
    @Autowired private GenericService<Brand, BrandRequest, BrandResponse> brandService;
    @Autowired private GenericService<Specification, SpecificationRequest, SpecificationResponse> specificationService;
    @Autowired private GenericService<Product, ProductRequest, ProductResponse> productService;
    @Autowired private GenericService<Variant, VariantRequest, VariantResponse> variantService;
    @Autowired private GenericService<Image, ImageRequest, ImageResponse> imageService;
    @Autowired private GenericService<OrderResource, OrderResourceRequest, OrderResourceResponse> orderResourceService;
    @Autowired private GenericService<OrderCancellationReason, OrderCancellationReasonRequest, OrderCancellationReasonResponse> orderCancellationReasonService;
    @Autowired private GenericService<Order, OrderRequest, OrderResponse> orderService;
    @Autowired private GenericService<PaymentMethod, PaymentMethodRequest, PaymentMethodResponse> paymentMethodService;
    @Autowired private GenericService<Room, RoomRequest, RoomResponse> roomService;
    @Autowired private GenericService<RewardStrategy, RewardStrategyRequest, RewardStrategyResponse> rewardStrategyService;

    @PostConstruct
    public void registerControllers() throws NoSuchMethodException {

        register("provinces", provinceController, provinceService, ProvinceRequest.class);

        register("districts", districtController, districtService.init(
                context.getBean(DistrictRepository.class),
                context.getBean(DistrictMapper.class),
                SearchFields.DISTRICT,
                ResourceName.DISTRICT
        ), DistrictRequest.class);

        register("wards", wardController, wardService.init(
                context.getBean(WardRepository.class),
                context.getBean(WardMapper.class),
                SearchFields.WARD,
                ResourceName.WARD
        ), WardRequest.class);

        register("addresses", addressController, addressService.init(
                context.getBean(AddressRepository.class),
                context.getBean(AddressMapper.class),
                SearchFields.ADDRESS,
                ResourceName.ADDRESS
        ), AddressRequest.class);

        register("users", userController, userService.init(
                context.getBean(UserRepository.class),
                context.getBean(UserMapper.class),
                SearchFields.USER,
                ResourceName.USER
        ), UserRequest.class);

        register("roles", roleController, roleService.init(
                context.getBean(RoleRepository.class),
                context.getBean(RoleMapper.class),
                SearchFields.ROLE,
                ResourceName.ROLE
        ), RoleRequest.class);

        register("properties", propertyController, propertyService.init(
                context.getBean(PropertyRepository.class),
                context.getBean(PropertyMapper.class),
                SearchFields.PROPERTY,
                ResourceName.PROPERTY
        ), PropertyRequest.class);

        register("categories", categoryController, categoryService.init(
                context.getBean(CategoryRepository.class),
                context.getBean(CategoryMapper.class),
                SearchFields.CATEGORY,
                ResourceName.CATEGORY
        ), CategoryRequest.class);

        register("tags", tagController, tagService.init(
                context.getBean(TagRepository.class),
                context.getBean(TagMapper.class),
                SearchFields.TAG,
                ResourceName.TAG
        ), TagRequest.class);

        register("guarantees", guaranteeController, guaranteeService.init(
                context.getBean(GuaranteeRepository.class),
                context.getBean(GuaranteeMapper.class),
                SearchFields.GUARANTEE,
                ResourceName.GUARANTEE
        ), GuaranteeRequest.class);

        register("units", unitController, unitService.init(
                context.getBean(UnitRepository.class),
                context.getBean(UnitMapper.class),
                SearchFields.UNIT,
                ResourceName.UNIT
        ), UnitRequest.class);

        register("suppliers", supplierController, supplierService.init(
                context.getBean(SupplierRepository.class),
                context.getBean(SupplierMapper.class),
                SearchFields.SUPPLIER,
                ResourceName.SUPPLIER
        ), SupplierRequest.class);

        register("brands", brandController, brandService.init(
                context.getBean(BrandRepository.class),
                context.getBean(BrandMapper.class),
                SearchFields.BRAND,
                ResourceName.BRAND
        ), BrandRequest.class);

        register("specifications", specificationController, specificationService.init(
                context.getBean(SpecificationRepository.class),
                context.getBean(SpecificationMapper.class),
                SearchFields.SPECIFICATION,
                ResourceName.SPECIFICATION
        ), SpecificationRequest.class);

        register("products", productController, productService.init(
                context.getBean(ProductRepository.class),
                context.getBean(ProductMapper.class),
                SearchFields.PRODUCT,
                ResourceName.PRODUCT
        ), ProductRequest.class);

        register("variants", variantController, variantService.init(
                context.getBean(VariantRepository.class),
                context.getBean(VariantMapper.class),
                SearchFields.VARIANT,
                ResourceName.VARIANT
        ), VariantRequest.class);

        register("images", imageController, imageService.init(
                context.getBean(ImageRepository.class),
                context.getBean(ImageMapper.class),
                SearchFields.IMAGE,
                ResourceName.IMAGE
        ), ImageRequest.class);


        register("order-resources", orderResourceController, orderResourceService.init(
                context.getBean(OrderResourceRepository.class),
                context.getBean(OrderResourceMapper.class),
                SearchFields.ORDER_RESOURCE,
                ResourceName.ORDER_RESOURCE
        ), OrderResourceRequest.class);

        register("order-cancellation-reasons", orderCancellationReasonController, orderCancellationReasonService.init(
                context.getBean(OrderCancellationReasonRepository.class),
                context.getBean(OrderCancellationReasonMapper.class),
                SearchFields.ORDER_CANCELLATION_REASON,
                ResourceName.ORDER_CANCELLATION_REASON
        ), OrderCancellationReasonRequest.class);

        register("orders", orderController, orderService.init(
                context.getBean(OrderRepository.class),
                context.getBean(OrderMapper.class),
                SearchFields.ORDER,
                ResourceName.ORDER
        ), OrderRequest.class);

        register("waybills", waybillController, context.getBean(WaybillService.class), WaybillRequest.class);

        register("reviews", reviewController, context.getBean(ReviewService.class), ReviewRequest.class);

        register("payment-methods", paymentMethodController, paymentMethodService.init(
                context.getBean(PaymentMethodRepository.class),
                context.getBean(PaymentMethodMapper.class),
                SearchFields.PAYMENT_METHOD,
                ResourceName.PAYMENT_METHOD
        ), PaymentMethodRequest.class);

        register("promotions", promotionController, context.getBean(PromotionService.class), PromotionRequest.class);

        register("rooms", roomController, roomService.init(
                context.getBean(RoomRepository.class),
                context.getBean(RoomMapper.class),
                SearchFields.ROOM,
                ResourceName.ROOM
        ), RoomRequest.class);

        register("reward-strategies", rewardStrategyController, rewardStrategyService.init(
                context.getBean(RewardStrategyRepository.class),
                context.getBean(RewardStrategyMapper.class),
                SearchFields.REWARD_STRATEGY,
                ResourceName.REWARD_STRATEGY
        ), RewardStrategyRequest.class);

    }

    private <I, O> void register(String resource,
                                 GenericController<I, O> controller,
                                 CrudService<Long, I, O> service,
                                 Class<I> requestType
    ) throws NoSuchMethodException {
        RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
        options.setPatternParser(new PathPatternParser());

        controller.setCrudService(service);
        controller.setRequestType(requestType);

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource)
                        .methods(RequestMethod.GET)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("getAllResources", int.class, int.class,
                        String.class, String.class, String.class, boolean.class)
        );

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource + "/{id}")
                        .methods(RequestMethod.GET)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("getResource", Long.class)
        );

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource)
                        .methods(RequestMethod.POST)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("createResource", JsonNode.class)
        );

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource + "/{id}")
                        .methods(RequestMethod.PUT)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("updateResource", Long.class, JsonNode.class)
        );

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource + "/{id}")
                        .methods(RequestMethod.DELETE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("deleteResource", Long.class)
        );

        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource)
                        .methods(RequestMethod.DELETE)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("deleteResources", List.class)
        );
        handlerMapping.registerMapping(
                RequestMappingInfo.paths("/api/" + resource + "/{id}/status")
                        .methods(RequestMethod.PUT)
                        .consumes(MediaType.APPLICATION_JSON_VALUE)
                        .produces(MediaType.APPLICATION_JSON_VALUE)
                        .options(options)
                        .build(),
                controller,
                controller.getClass().getMethod("updateStatus", Long.class, JsonNode.class)
        );
    }
}
