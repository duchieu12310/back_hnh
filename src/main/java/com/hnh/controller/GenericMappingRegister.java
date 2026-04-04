package com.hnh.controller;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.address.AddressRequest;
import com.hnh.dto.address.AddressResponse;
import com.hnh.dto.address.DistrictRequest;
import com.hnh.dto.address.DistrictResponse;
import com.hnh.dto.address.ProvinceRequest;
import com.hnh.dto.address.ProvinceResponse;
import com.hnh.dto.address.WardRequest;
import com.hnh.dto.address.WardResponse;
import com.hnh.dto.authentication.RoleRequest;
import com.hnh.dto.authentication.RoleResponse;
import com.hnh.dto.authentication.UserRequest;
import com.hnh.dto.authentication.UserResponse;
import com.hnh.dto.cashbook.PaymentMethodRequest;
import com.hnh.dto.cashbook.PaymentMethodResponse;
import com.hnh.dto.chat.RoomRequest;
import com.hnh.dto.chat.RoomResponse;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.hnh.dto.customer.CustomerGroupRequest;
// import com.hnh.dto.customer.CustomerGroupResponse;
// import com.hnh.dto.customer.CustomerRequest;
// import com.hnh.dto.customer.CustomerResourceRequest;
// import com.hnh.dto.customer.CustomerResourceResponse;
// import com.hnh.dto.customer.CustomerResponse;
// import com.hnh.dto.customer.CustomerStatusRequest;
// import com.hnh.dto.customer.CustomerStatusResponse;
// import com.hnh.dto.employee.DepartmentRequest;
// import com.hnh.dto.employee.DepartmentResponse;
// import com.hnh.dto.employee.EmployeeRequest;
// import com.hnh.dto.employee.EmployeeResponse;
// import com.hnh.dto.employee.JobLevelRequest;
// import com.hnh.dto.employee.JobLevelResponse;
// import com.hnh.dto.employee.JobTitleRequest;
// import com.hnh.dto.employee.JobTitleResponse;
// import com.hnh.dto.employee.JobTypeRequest;
// import com.hnh.dto.employee.JobTypeResponse;
// import com.hnh.dto.employee.OfficeRequest;
// import com.hnh.dto.employee.OfficeResponse;
import com.hnh.dto.general.ImageRequest;
import com.hnh.dto.general.ImageResponse;
import com.hnh.dto.inventory.CountRequest;
import com.hnh.dto.inventory.CountResponse;
import com.hnh.dto.inventory.DestinationRequest;
import com.hnh.dto.inventory.DestinationResponse;
import com.hnh.dto.inventory.DocketReasonRequest;
import com.hnh.dto.inventory.DocketReasonResponse;
import com.hnh.dto.inventory.DocketRequest;
import com.hnh.dto.inventory.DocketResponse;
import com.hnh.dto.inventory.ProductInventoryLimitRequest;
import com.hnh.dto.inventory.ProductInventoryLimitResponse;
import com.hnh.dto.inventory.PurchaseOrderRequest;
import com.hnh.dto.inventory.PurchaseOrderResponse;
import com.hnh.dto.inventory.StorageLocationRequest;
import com.hnh.dto.inventory.StorageLocationResponse;
import com.hnh.dto.inventory.TransferRequest;
import com.hnh.dto.inventory.TransferResponse;
import com.hnh.dto.inventory.VariantInventoryLimitRequest;
import com.hnh.dto.inventory.VariantInventoryLimitResponse;
import com.hnh.dto.inventory.WarehouseRequest;
import com.hnh.dto.inventory.WarehouseResponse;
import com.hnh.dto.order.OrderCancellationReasonRequest;
import com.hnh.dto.order.OrderCancellationReasonResponse;
import com.hnh.dto.order.OrderRequest;
import com.hnh.dto.order.OrderResourceRequest;
import com.hnh.dto.order.OrderResourceResponse;
import com.hnh.dto.order.OrderResponse;
import com.hnh.dto.product.BrandRequest;
import com.hnh.dto.product.BrandResponse;
import com.hnh.dto.product.CategoryRequest;
import com.hnh.dto.product.CategoryResponse;
import com.hnh.dto.product.GuaranteeRequest;
import com.hnh.dto.product.GuaranteeResponse;
import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.dto.product.PropertyRequest;
import com.hnh.dto.product.PropertyResponse;
import com.hnh.dto.product.SpecificationRequest;
import com.hnh.dto.product.SpecificationResponse;
import com.hnh.dto.product.SupplierRequest;
import com.hnh.dto.product.SupplierResponse;
import com.hnh.dto.product.TagRequest;
import com.hnh.dto.product.TagResponse;
import com.hnh.dto.product.UnitRequest;
import com.hnh.dto.product.UnitResponse;
import com.hnh.dto.product.VariantRequest;
import com.hnh.dto.product.VariantResponse;
import com.hnh.dto.promotion.PromotionRequest;
import com.hnh.dto.promotion.PromotionResponse;
import com.hnh.dto.review.ReviewRequest;
import com.hnh.dto.review.ReviewResponse;
import com.hnh.dto.reward.RewardStrategyRequest;
import com.hnh.dto.reward.RewardStrategyResponse;
import com.hnh.dto.waybill.WaybillRequest;
import com.hnh.dto.waybill.WaybillResponse;
import com.hnh.entity.address.Address;
import com.hnh.entity.address.District;
import com.hnh.entity.address.Ward;
import com.hnh.entity.authentication.Role;
import com.hnh.entity.authentication.User;
import com.hnh.entity.cashbook.PaymentMethod;
import com.hnh.entity.chat.Room;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.hnh.entity.customer.Customer;
// import com.hnh.entity.customer.CustomerGroup;
// import com.hnh.entity.customer.CustomerResource;
// import com.hnh.entity.customer.CustomerStatus;
// import com.hnh.entity.employee.Department;
// import com.hnh.entity.employee.Employee;
// import com.hnh.entity.employee.JobLevel;
// import com.hnh.entity.employee.JobTitle;
// import com.hnh.entity.employee.JobType;
// import com.hnh.entity.employee.Office;
import com.hnh.entity.general.Image;
import com.hnh.entity.inventory.Count;
import com.hnh.entity.inventory.Destination;
import com.hnh.entity.inventory.DocketReason;
import com.hnh.entity.inventory.ProductInventoryLimit;
import com.hnh.entity.inventory.PurchaseOrder;
import com.hnh.entity.inventory.StorageLocation;
import com.hnh.entity.inventory.Transfer;
import com.hnh.entity.inventory.VariantInventoryLimit;
import com.hnh.entity.inventory.Warehouse;
import com.hnh.entity.order.Order;
import com.hnh.entity.order.OrderCancellationReason;
import com.hnh.entity.order.OrderResource;
import com.hnh.entity.product.Brand;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Guarantee;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Property;
import com.hnh.entity.product.Specification;
import com.hnh.entity.product.Supplier;
import com.hnh.entity.product.Tag;
import com.hnh.entity.product.Unit;
import com.hnh.entity.product.Variant;
import com.hnh.entity.reward.RewardStrategy;
import com.hnh.mapper.address.AddressMapper;
import com.hnh.mapper.address.DistrictMapper;
import com.hnh.mapper.address.WardMapper;
import com.hnh.mapper.authentication.RoleMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.mapper.cashbook.PaymentMethodMapper;
import com.hnh.mapper.chat.RoomMapper;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.hnh.mapper.customer.CustomerGroupMapper;
// import com.hnh.mapper.customer.CustomerMapper;
// import com.hnh.mapper.customer.CustomerResourceMapper;
// import com.hnh.mapper.customer.CustomerStatusMapper;
// import com.hnh.mapper.employee.DepartmentMapper;
// import com.hnh.mapper.employee.EmployeeMapper;
// import com.hnh.mapper.employee.JobLevelMapper;
// import com.hnh.mapper.employee.JobTitleMapper;
// import com.hnh.mapper.employee.JobTypeMapper;
// import com.hnh.mapper.employee.OfficeMapper;
import com.hnh.mapper.general.ImageMapper;
import com.hnh.mapper.inventory.CountMapper;
import com.hnh.mapper.inventory.DestinationMapper;
import com.hnh.mapper.inventory.DocketReasonMapper;
import com.hnh.mapper.inventory.ProductInventoryLimitMapper;
import com.hnh.mapper.inventory.PurchaseOrderMapper;
import com.hnh.mapper.inventory.StorageLocationMapper;
import com.hnh.mapper.inventory.TransferMapper;
import com.hnh.mapper.inventory.VariantInventoryLimitMapper;
import com.hnh.mapper.inventory.WarehouseMapper;
import com.hnh.mapper.order.OrderCancellationReasonMapper;
import com.hnh.mapper.order.OrderMapper;
import com.hnh.mapper.order.OrderResourceMapper;
import com.hnh.mapper.product.BrandMapper;
import com.hnh.mapper.product.CategoryMapper;
import com.hnh.mapper.product.GuaranteeMapper;
import com.hnh.mapper.product.ProductMapper;
import com.hnh.mapper.product.PropertyMapper;
import com.hnh.mapper.product.SpecificationMapper;
import com.hnh.mapper.product.SupplierMapper;
import com.hnh.mapper.product.TagMapper;
import com.hnh.mapper.product.UnitMapper;
import com.hnh.mapper.product.VariantMapper;
import com.hnh.mapper.reward.RewardStrategyMapper;
import com.hnh.repository.address.AddressRepository;
import com.hnh.repository.address.DistrictRepository;
import com.hnh.repository.address.WardRepository;
import com.hnh.repository.authentication.RoleRepository;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.repository.cashbook.PaymentMethodRepository;
import com.hnh.repository.chat.RoomRepository;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.hnh.repository.customer.CustomerGroupRepository;
// import com.hnh.repository.customer.CustomerRepository;
// import com.hnh.repository.customer.CustomerResourceRepository;
// import com.hnh.repository.customer.CustomerStatusRepository;
// import com.hnh.repository.employee.DepartmentRepository;
// import com.hnh.repository.employee.EmployeeRepository;
// import com.hnh.repository.employee.JobLevelRepository;
// import com.hnh.repository.employee.JobTitleRepository;
// import com.hnh.repository.employee.JobTypeRepository;
// import com.hnh.repository.employee.OfficeRepository;
import com.hnh.repository.general.ImageRepository;
import com.hnh.repository.inventory.CountRepository;
import com.hnh.repository.inventory.DestinationRepository;
import com.hnh.repository.inventory.DocketReasonRepository;
import com.hnh.repository.inventory.ProductInventoryLimitRepository;
import com.hnh.repository.inventory.PurchaseOrderRepository;
import com.hnh.repository.inventory.StorageLocationRepository;
import com.hnh.repository.inventory.TransferRepository;
import com.hnh.repository.inventory.VariantInventoryLimitRepository;
import com.hnh.repository.inventory.WarehouseRepository;
import com.hnh.repository.order.OrderCancellationReasonRepository;
import com.hnh.repository.order.OrderRepository;
import com.hnh.repository.order.OrderResourceRepository;
import com.hnh.repository.product.BrandRepository;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.repository.product.GuaranteeRepository;
import com.hnh.repository.product.ProductRepository;
import com.hnh.repository.product.PropertyRepository;
import com.hnh.repository.product.SpecificationRepository;
import com.hnh.repository.product.SupplierRepository;
import com.hnh.repository.product.TagRepository;
import com.hnh.repository.product.UnitRepository;
import com.hnh.repository.product.VariantRepository;
import com.hnh.repository.reward.RewardStrategyRepository;
import com.hnh.service.CrudService;
import com.hnh.service.GenericService;
import com.hnh.service.address.ProvinceService;
import com.hnh.service.inventory.DocketService;
import com.hnh.service.promotion.PromotionService;
import com.hnh.service.review.ReviewService;
import com.hnh.service.waybill.WaybillService;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class GenericMappingRegister {

    private ApplicationContext context;
    private RequestMappingHandlerMapping handlerMapping;

    // Controllers
    private GenericController<ProvinceRequest, ProvinceResponse> provinceController;
    private GenericController<DistrictRequest, DistrictResponse> districtController;
    private GenericController<WardRequest, WardResponse> wardController;
    private GenericController<AddressRequest, AddressResponse> addressController;
    private GenericController<UserRequest, UserResponse> userController;
    private GenericController<RoleRequest, RoleResponse> roleController;
    // TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
    // private GenericController<OfficeRequest, OfficeResponse> officeController;
    // private GenericController<DepartmentRequest, DepartmentResponse> departmentController;
    // private GenericController<JobLevelRequest, JobLevelResponse> jobLevelController;
    // private GenericController<JobTypeRequest, JobTypeResponse> jobTypeController;
    // private GenericController<JobTitleRequest, JobTitleResponse> jobTitleController;
    // private GenericController<EmployeeRequest, EmployeeResponse> employeeController;
    // private GenericController<CustomerGroupRequest, CustomerGroupResponse> customerGroupController;
    // private GenericController<CustomerResourceRequest, CustomerResourceResponse> customerResourceController;
    // private GenericController<CustomerStatusRequest, CustomerStatusResponse> customerStatusController;
    // private GenericController<CustomerRequest, CustomerResponse> customerController;
    private GenericController<PropertyRequest, PropertyResponse> propertyController;
    private GenericController<CategoryRequest, CategoryResponse> categoryController;
    private GenericController<TagRequest, TagResponse> tagController;
    private GenericController<GuaranteeRequest, GuaranteeResponse> guaranteeController;
    private GenericController<UnitRequest, UnitResponse> unitController;
    private GenericController<SupplierRequest, SupplierResponse> supplierController;
    private GenericController<BrandRequest, BrandResponse> brandController;
    private GenericController<SpecificationRequest, SpecificationResponse> specificationController;
    private GenericController<ProductRequest, ProductResponse> productController;
    private GenericController<VariantRequest, VariantResponse> variantController;
    private GenericController<ImageRequest, ImageResponse> imageController;
    private GenericController<ProductInventoryLimitRequest, ProductInventoryLimitResponse> productInventoryLimitController;
    private GenericController<VariantInventoryLimitRequest, VariantInventoryLimitResponse> variantInventoryLimitController;
    private GenericController<WarehouseRequest, WarehouseResponse> warehouseController;
    private GenericController<CountRequest, CountResponse> countController;
    private GenericController<DestinationRequest, DestinationResponse> destinationController;
    private GenericController<DocketReasonRequest, DocketReasonResponse> docketReasonController;
    private GenericController<TransferRequest, TransferResponse> transferController;
    private GenericController<DocketRequest, DocketResponse> docketController;
    private GenericController<StorageLocationRequest, StorageLocationResponse> storageLocationController;
    private GenericController<PurchaseOrderRequest, PurchaseOrderResponse> purchaseOrderController;
    private GenericController<OrderResourceRequest, OrderResourceResponse> orderResourceController;
    private GenericController<OrderCancellationReasonRequest, OrderCancellationReasonResponse> orderCancellationReasonController;
    private GenericController<OrderRequest, OrderResponse> orderController;
    private GenericController<WaybillRequest, WaybillResponse> waybillController;
    private GenericController<ReviewRequest, ReviewResponse> reviewController;
    private GenericController<PaymentMethodRequest, PaymentMethodResponse> paymentMethodController;
    private GenericController<PromotionRequest, PromotionResponse> promotionController;
    private GenericController<RoomRequest, RoomResponse> roomController;
    private GenericController<RewardStrategyRequest, RewardStrategyResponse> rewardStrategyController;

    // Services
    private GenericService<District, DistrictRequest, DistrictResponse> districtService;
    private GenericService<Ward, WardRequest, WardResponse> wardService;
    private GenericService<Address, AddressRequest, AddressResponse> addressService;
    private GenericService<User, UserRequest, UserResponse> userService;
    private GenericService<Role, RoleRequest, RoleResponse> roleService;
    // TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
    // private GenericService<Office, OfficeRequest, OfficeResponse> officeService;
    // private GenericService<Department, DepartmentRequest, DepartmentResponse> departmentService;
    // private GenericService<JobLevel, JobLevelRequest, JobLevelResponse> jobLevelService;
    // private GenericService<JobType, JobTypeRequest, JobTypeResponse> jobTypeService;
    // private GenericService<JobTitle, JobTitleRequest, JobTitleResponse> jobTitleService;
    // private GenericService<Employee, EmployeeRequest, EmployeeResponse> employeeService;
    // private GenericService<CustomerGroup, CustomerGroupRequest, CustomerGroupResponse> customerGroupService;
    // private GenericService<CustomerResource, CustomerResourceRequest, CustomerResourceResponse> customerResourceService;
    // private GenericService<CustomerStatus, CustomerStatusRequest, CustomerStatusResponse> customerStatusService;
    // private GenericService<Customer, CustomerRequest, CustomerResponse> customerService;
    private GenericService<Property, PropertyRequest, PropertyResponse> propertyService;
    private GenericService<Category, CategoryRequest, CategoryResponse> categoryService;
    private GenericService<Tag, TagRequest, TagResponse> tagService;
    private GenericService<Guarantee, GuaranteeRequest, GuaranteeResponse> guaranteeService;
    private GenericService<Unit, UnitRequest, UnitResponse> unitService;
    private GenericService<Supplier, SupplierRequest, SupplierResponse> supplierService;
    private GenericService<Brand, BrandRequest, BrandResponse> brandService;
    private GenericService<Specification, SpecificationRequest, SpecificationResponse> specificationService;
    private GenericService<Product, ProductRequest, ProductResponse> productService;
    private GenericService<Variant, VariantRequest, VariantResponse> variantService;
    private GenericService<Image, ImageRequest, ImageResponse> imageService;
    private GenericService<ProductInventoryLimit, ProductInventoryLimitRequest, ProductInventoryLimitResponse> productInventoryLimitService;
    private GenericService<VariantInventoryLimit, VariantInventoryLimitRequest, VariantInventoryLimitResponse> variantInventoryLimitService;
    private GenericService<Warehouse, WarehouseRequest, WarehouseResponse> warehouseService;
    private GenericService<Count, CountRequest, CountResponse> countService;
    private GenericService<Destination, DestinationRequest, DestinationResponse> destinationService;
    private GenericService<DocketReason, DocketReasonRequest, DocketReasonResponse> docketReasonService;
    private GenericService<Transfer, TransferRequest, TransferResponse> transferService;
    private GenericService<StorageLocation, StorageLocationRequest, StorageLocationResponse> storageLocationService;
    private GenericService<PurchaseOrder, PurchaseOrderRequest, PurchaseOrderResponse> purchaseOrderService;
    private GenericService<OrderResource, OrderResourceRequest, OrderResourceResponse> orderResourceService;
    private GenericService<OrderCancellationReason, OrderCancellationReasonRequest, OrderCancellationReasonResponse> orderCancellationReasonService;
    private GenericService<Order, OrderRequest, OrderResponse> orderService;
    private GenericService<PaymentMethod, PaymentMethodRequest, PaymentMethodResponse> paymentMethodService;
    private GenericService<Room, RoomRequest, RoomResponse> roomService;
    private GenericService<RewardStrategy, RewardStrategyRequest, RewardStrategyResponse> rewardStrategyService;

    @PostConstruct
    public void registerControllers() throws NoSuchMethodException {

        register("provinces", provinceController, context.getBean(ProvinceService.class), ProvinceRequest.class);

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

        // TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
        // register("offices", officeController, officeService.init(
        //         context.getBean(OfficeRepository.class),
        //         context.getBean(OfficeMapper.class),
        //         SearchFields.OFFICE,
        //         ResourceName.OFFICE
        // ), OfficeRequest.class);

        // register("departments", departmentController, departmentService.init(
        //         context.getBean(DepartmentRepository.class),
        //         context.getBean(DepartmentMapper.class),
        //         SearchFields.DEPARTMENT,
        //         ResourceName.DEPARTMENT
        // ), DepartmentRequest.class);

        // register("job-levels", jobLevelController, jobLevelService.init(
        //         context.getBean(JobLevelRepository.class),
        //         context.getBean(JobLevelMapper.class),
        //         SearchFields.JOB_LEVEL,
        //         ResourceName.JOB_LEVEL
        // ), JobLevelRequest.class);

        // register("job-titles", jobTitleController, jobTitleService.init(
        //         context.getBean(JobTitleRepository.class),
        //         context.getBean(JobTitleMapper.class),
        //         SearchFields.JOB_TITLE,
        //         ResourceName.JOB_TITLE
        // ), JobTitleRequest.class);

        // register("job-types", jobTypeController, jobTypeService.init(
        //         context.getBean(JobTypeRepository.class),
        //         context.getBean(JobTypeMapper.class),
        //         SearchFields.JOB_TYPE,
        //         ResourceName.JOB_TYPE
        // ), JobTypeRequest.class);

        // register("employees", employeeController, employeeService.init(
        //         context.getBean(EmployeeRepository.class),
        //         context.getBean(EmployeeMapper.class),
        //         SearchFields.EMPLOYEE,
        //         ResourceName.EMPLOYEE
        // ), EmployeeRequest.class);

        // register("customer-groups", customerGroupController, customerGroupService.init(
        //         context.getBean(CustomerGroupRepository.class),
        //         context.getBean(CustomerGroupMapper.class),
        //         SearchFields.CUSTOMER_GROUP,
        //         ResourceName.CUSTOMER_GROUP
        // ), CustomerGroupRequest.class);

        // register("customer-resources", customerResourceController, customerResourceService.init(
        //         context.getBean(CustomerResourceRepository.class),
        //         context.getBean(CustomerResourceMapper.class),
        //         SearchFields.CUSTOMER_RESOURCE,
        //         ResourceName.CUSTOMER_RESOURCE
        // ), CustomerResourceRequest.class);

        // register("customer-status", customerStatusController, customerStatusService.init(
        //         context.getBean(CustomerStatusRepository.class),
        //         context.getBean(CustomerStatusMapper.class),
        //         SearchFields.CUSTOMER_STATUS,
        //         ResourceName.CUSTOMER_STATUS
        // ), CustomerStatusRequest.class);

        // register("customers", customerController, customerService.init(
        //         context.getBean(CustomerRepository.class),
        //         context.getBean(CustomerMapper.class),
        //         SearchFields.CUSTOMER,
        //         ResourceName.CUSTOMER
        // ), CustomerRequest.class);

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

        register("product-inventory-limits", productInventoryLimitController, productInventoryLimitService.init(
                context.getBean(ProductInventoryLimitRepository.class),
                context.getBean(ProductInventoryLimitMapper.class),
                SearchFields.PRODUCT_INVENTORY_LIMIT,
                ResourceName.PRODUCT_INVENTORY_LIMIT
        ), ProductInventoryLimitRequest.class);

        register("variant-inventory-limits", variantInventoryLimitController, variantInventoryLimitService.init(
                context.getBean(VariantInventoryLimitRepository.class),
                context.getBean(VariantInventoryLimitMapper.class),
                SearchFields.VARIANT_INVENTORY_LIMIT,
                ResourceName.VARIANT_INVENTORY_LIMIT
        ), VariantInventoryLimitRequest.class);

        register("warehouses", warehouseController, warehouseService.init(
                context.getBean(WarehouseRepository.class),
                context.getBean(WarehouseMapper.class),
                SearchFields.WAREHOUSE,
                ResourceName.WAREHOUSE
        ), WarehouseRequest.class);

        register("counts", countController, countService.init(
                context.getBean(CountRepository.class),
                context.getBean(CountMapper.class),
                SearchFields.COUNT,
                ResourceName.COUNT
        ), CountRequest.class);

        register("destinations", destinationController, destinationService.init(
                context.getBean(DestinationRepository.class),
                context.getBean(DestinationMapper.class),
                SearchFields.DESTINATION,
                ResourceName.DESTINATION
        ), DestinationRequest.class);

        register("docket-reasons", docketReasonController, docketReasonService.init(
                context.getBean(DocketReasonRepository.class),
                context.getBean(DocketReasonMapper.class),
                SearchFields.DOCKET_REASON,
                ResourceName.DOCKET_REASON
        ), DocketReasonRequest.class);

        register("transfers", transferController, transferService.init(
                context.getBean(TransferRepository.class),
                context.getBean(TransferMapper.class),
                SearchFields.TRANSFER,
                ResourceName.TRANSFER
        ), TransferRequest.class);

        register("dockets", docketController, context.getBean(DocketService.class), DocketRequest.class);

        register("storage-locations", storageLocationController, storageLocationService.init(
                context.getBean(StorageLocationRepository.class),
                context.getBean(StorageLocationMapper.class),
                SearchFields.STORAGE_LOCATION,
                ResourceName.STORAGE_LOCATION
        ), StorageLocationRequest.class);

        register("purchase-orders", purchaseOrderController, purchaseOrderService.init(
                context.getBean(PurchaseOrderRepository.class),
                context.getBean(PurchaseOrderMapper.class),
                SearchFields.PURCHASE_ORDER,
                ResourceName.PURCHASE_ORDER
        ), PurchaseOrderRequest.class);

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

