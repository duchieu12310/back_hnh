package com.thatannhien.controller;

import com.thatannhien.constant.ResourceName;
import com.thatannhien.constant.SearchFields;
import com.thatannhien.dto.address.AddressRequest;
import com.thatannhien.dto.address.AddressResponse;
import com.thatannhien.dto.address.DistrictRequest;
import com.thatannhien.dto.address.DistrictResponse;
import com.thatannhien.dto.address.ProvinceRequest;
import com.thatannhien.dto.address.ProvinceResponse;
import com.thatannhien.dto.address.WardRequest;
import com.thatannhien.dto.address.WardResponse;
import com.thatannhien.dto.authentication.RoleRequest;
import com.thatannhien.dto.authentication.RoleResponse;
import com.thatannhien.dto.authentication.UserRequest;
import com.thatannhien.dto.authentication.UserResponse;
import com.thatannhien.dto.cashbook.PaymentMethodRequest;
import com.thatannhien.dto.cashbook.PaymentMethodResponse;
import com.thatannhien.dto.chat.RoomRequest;
import com.thatannhien.dto.chat.RoomResponse;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.thatannhien.dto.customer.CustomerGroupRequest;
// import com.thatannhien.dto.customer.CustomerGroupResponse;
// import com.thatannhien.dto.customer.CustomerRequest;
// import com.thatannhien.dto.customer.CustomerResourceRequest;
// import com.thatannhien.dto.customer.CustomerResourceResponse;
// import com.thatannhien.dto.customer.CustomerResponse;
// import com.thatannhien.dto.customer.CustomerStatusRequest;
// import com.thatannhien.dto.customer.CustomerStatusResponse;
// import com.thatannhien.dto.employee.DepartmentRequest;
// import com.thatannhien.dto.employee.DepartmentResponse;
// import com.thatannhien.dto.employee.EmployeeRequest;
// import com.thatannhien.dto.employee.EmployeeResponse;
// import com.thatannhien.dto.employee.JobLevelRequest;
// import com.thatannhien.dto.employee.JobLevelResponse;
// import com.thatannhien.dto.employee.JobTitleRequest;
// import com.thatannhien.dto.employee.JobTitleResponse;
// import com.thatannhien.dto.employee.JobTypeRequest;
// import com.thatannhien.dto.employee.JobTypeResponse;
// import com.thatannhien.dto.employee.OfficeRequest;
// import com.thatannhien.dto.employee.OfficeResponse;
import com.thatannhien.dto.general.ImageRequest;
import com.thatannhien.dto.general.ImageResponse;
import com.thatannhien.dto.inventory.CountRequest;
import com.thatannhien.dto.inventory.CountResponse;
import com.thatannhien.dto.inventory.DestinationRequest;
import com.thatannhien.dto.inventory.DestinationResponse;
import com.thatannhien.dto.inventory.DocketReasonRequest;
import com.thatannhien.dto.inventory.DocketReasonResponse;
import com.thatannhien.dto.inventory.DocketRequest;
import com.thatannhien.dto.inventory.DocketResponse;
import com.thatannhien.dto.inventory.ProductInventoryLimitRequest;
import com.thatannhien.dto.inventory.ProductInventoryLimitResponse;
import com.thatannhien.dto.inventory.PurchaseOrderRequest;
import com.thatannhien.dto.inventory.PurchaseOrderResponse;
import com.thatannhien.dto.inventory.StorageLocationRequest;
import com.thatannhien.dto.inventory.StorageLocationResponse;
import com.thatannhien.dto.inventory.TransferRequest;
import com.thatannhien.dto.inventory.TransferResponse;
import com.thatannhien.dto.inventory.VariantInventoryLimitRequest;
import com.thatannhien.dto.inventory.VariantInventoryLimitResponse;
import com.thatannhien.dto.inventory.WarehouseRequest;
import com.thatannhien.dto.inventory.WarehouseResponse;
import com.thatannhien.dto.order.OrderCancellationReasonRequest;
import com.thatannhien.dto.order.OrderCancellationReasonResponse;
import com.thatannhien.dto.order.OrderRequest;
import com.thatannhien.dto.order.OrderResourceRequest;
import com.thatannhien.dto.order.OrderResourceResponse;
import com.thatannhien.dto.order.OrderResponse;
import com.thatannhien.dto.product.BrandRequest;
import com.thatannhien.dto.product.BrandResponse;
import com.thatannhien.dto.product.CategoryRequest;
import com.thatannhien.dto.product.CategoryResponse;
import com.thatannhien.dto.product.GuaranteeRequest;
import com.thatannhien.dto.product.GuaranteeResponse;
import com.thatannhien.dto.product.ProductRequest;
import com.thatannhien.dto.product.ProductResponse;
import com.thatannhien.dto.product.PropertyRequest;
import com.thatannhien.dto.product.PropertyResponse;
import com.thatannhien.dto.product.SpecificationRequest;
import com.thatannhien.dto.product.SpecificationResponse;
import com.thatannhien.dto.product.SupplierRequest;
import com.thatannhien.dto.product.SupplierResponse;
import com.thatannhien.dto.product.TagRequest;
import com.thatannhien.dto.product.TagResponse;
import com.thatannhien.dto.product.UnitRequest;
import com.thatannhien.dto.product.UnitResponse;
import com.thatannhien.dto.product.VariantRequest;
import com.thatannhien.dto.product.VariantResponse;
import com.thatannhien.dto.promotion.PromotionRequest;
import com.thatannhien.dto.promotion.PromotionResponse;
import com.thatannhien.dto.review.ReviewRequest;
import com.thatannhien.dto.review.ReviewResponse;
import com.thatannhien.dto.reward.RewardStrategyRequest;
import com.thatannhien.dto.reward.RewardStrategyResponse;
import com.thatannhien.dto.waybill.WaybillRequest;
import com.thatannhien.dto.waybill.WaybillResponse;
import com.thatannhien.entity.address.Address;
import com.thatannhien.entity.address.District;
import com.thatannhien.entity.address.Ward;
import com.thatannhien.entity.authentication.Role;
import com.thatannhien.entity.authentication.User;
import com.thatannhien.entity.cashbook.PaymentMethod;
import com.thatannhien.entity.chat.Room;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.thatannhien.entity.customer.Customer;
// import com.thatannhien.entity.customer.CustomerGroup;
// import com.thatannhien.entity.customer.CustomerResource;
// import com.thatannhien.entity.customer.CustomerStatus;
// import com.thatannhien.entity.employee.Department;
// import com.thatannhien.entity.employee.Employee;
// import com.thatannhien.entity.employee.JobLevel;
// import com.thatannhien.entity.employee.JobTitle;
// import com.thatannhien.entity.employee.JobType;
// import com.thatannhien.entity.employee.Office;
import com.thatannhien.entity.general.Image;
import com.thatannhien.entity.inventory.Count;
import com.thatannhien.entity.inventory.Destination;
import com.thatannhien.entity.inventory.DocketReason;
import com.thatannhien.entity.inventory.ProductInventoryLimit;
import com.thatannhien.entity.inventory.PurchaseOrder;
import com.thatannhien.entity.inventory.StorageLocation;
import com.thatannhien.entity.inventory.Transfer;
import com.thatannhien.entity.inventory.VariantInventoryLimit;
import com.thatannhien.entity.inventory.Warehouse;
import com.thatannhien.entity.order.Order;
import com.thatannhien.entity.order.OrderCancellationReason;
import com.thatannhien.entity.order.OrderResource;
import com.thatannhien.entity.product.Brand;
import com.thatannhien.entity.product.Category;
import com.thatannhien.entity.product.Guarantee;
import com.thatannhien.entity.product.Product;
import com.thatannhien.entity.product.Property;
import com.thatannhien.entity.product.Specification;
import com.thatannhien.entity.product.Supplier;
import com.thatannhien.entity.product.Tag;
import com.thatannhien.entity.product.Unit;
import com.thatannhien.entity.product.Variant;
import com.thatannhien.entity.reward.RewardStrategy;
import com.thatannhien.mapper.address.AddressMapper;
import com.thatannhien.mapper.address.DistrictMapper;
import com.thatannhien.mapper.address.WardMapper;
import com.thatannhien.mapper.authentication.RoleMapper;
import com.thatannhien.mapper.authentication.UserMapper;
import com.thatannhien.mapper.cashbook.PaymentMethodMapper;
import com.thatannhien.mapper.chat.RoomMapper;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.thatannhien.mapper.customer.CustomerGroupMapper;
// import com.thatannhien.mapper.customer.CustomerMapper;
// import com.thatannhien.mapper.customer.CustomerResourceMapper;
// import com.thatannhien.mapper.customer.CustomerStatusMapper;
// import com.thatannhien.mapper.employee.DepartmentMapper;
// import com.thatannhien.mapper.employee.EmployeeMapper;
// import com.thatannhien.mapper.employee.JobLevelMapper;
// import com.thatannhien.mapper.employee.JobTitleMapper;
// import com.thatannhien.mapper.employee.JobTypeMapper;
// import com.thatannhien.mapper.employee.OfficeMapper;
import com.thatannhien.mapper.general.ImageMapper;
import com.thatannhien.mapper.inventory.CountMapper;
import com.thatannhien.mapper.inventory.DestinationMapper;
import com.thatannhien.mapper.inventory.DocketReasonMapper;
import com.thatannhien.mapper.inventory.ProductInventoryLimitMapper;
import com.thatannhien.mapper.inventory.PurchaseOrderMapper;
import com.thatannhien.mapper.inventory.StorageLocationMapper;
import com.thatannhien.mapper.inventory.TransferMapper;
import com.thatannhien.mapper.inventory.VariantInventoryLimitMapper;
import com.thatannhien.mapper.inventory.WarehouseMapper;
import com.thatannhien.mapper.order.OrderCancellationReasonMapper;
import com.thatannhien.mapper.order.OrderMapper;
import com.thatannhien.mapper.order.OrderResourceMapper;
import com.thatannhien.mapper.product.BrandMapper;
import com.thatannhien.mapper.product.CategoryMapper;
import com.thatannhien.mapper.product.GuaranteeMapper;
import com.thatannhien.mapper.product.ProductMapper;
import com.thatannhien.mapper.product.PropertyMapper;
import com.thatannhien.mapper.product.SpecificationMapper;
import com.thatannhien.mapper.product.SupplierMapper;
import com.thatannhien.mapper.product.TagMapper;
import com.thatannhien.mapper.product.UnitMapper;
import com.thatannhien.mapper.product.VariantMapper;
import com.thatannhien.mapper.reward.RewardStrategyMapper;
import com.thatannhien.repository.address.AddressRepository;
import com.thatannhien.repository.address.DistrictRepository;
import com.thatannhien.repository.address.WardRepository;
import com.thatannhien.repository.authentication.RoleRepository;
import com.thatannhien.repository.authentication.UserRepository;
import com.thatannhien.repository.cashbook.PaymentMethodRepository;
import com.thatannhien.repository.chat.RoomRepository;
// TODO: TẠM THỜI COMMENT - ĐƠN GIẢN HÓA HỆ THỐNG (XÓA CUSTOMER VÀ EMPLOYEE)
// import com.thatannhien.repository.customer.CustomerGroupRepository;
// import com.thatannhien.repository.customer.CustomerRepository;
// import com.thatannhien.repository.customer.CustomerResourceRepository;
// import com.thatannhien.repository.customer.CustomerStatusRepository;
// import com.thatannhien.repository.employee.DepartmentRepository;
// import com.thatannhien.repository.employee.EmployeeRepository;
// import com.thatannhien.repository.employee.JobLevelRepository;
// import com.thatannhien.repository.employee.JobTitleRepository;
// import com.thatannhien.repository.employee.JobTypeRepository;
// import com.thatannhien.repository.employee.OfficeRepository;
import com.thatannhien.repository.general.ImageRepository;
import com.thatannhien.repository.inventory.CountRepository;
import com.thatannhien.repository.inventory.DestinationRepository;
import com.thatannhien.repository.inventory.DocketReasonRepository;
import com.thatannhien.repository.inventory.ProductInventoryLimitRepository;
import com.thatannhien.repository.inventory.PurchaseOrderRepository;
import com.thatannhien.repository.inventory.StorageLocationRepository;
import com.thatannhien.repository.inventory.TransferRepository;
import com.thatannhien.repository.inventory.VariantInventoryLimitRepository;
import com.thatannhien.repository.inventory.WarehouseRepository;
import com.thatannhien.repository.order.OrderCancellationReasonRepository;
import com.thatannhien.repository.order.OrderRepository;
import com.thatannhien.repository.order.OrderResourceRepository;
import com.thatannhien.repository.product.BrandRepository;
import com.thatannhien.repository.product.CategoryRepository;
import com.thatannhien.repository.product.GuaranteeRepository;
import com.thatannhien.repository.product.ProductRepository;
import com.thatannhien.repository.product.PropertyRepository;
import com.thatannhien.repository.product.SpecificationRepository;
import com.thatannhien.repository.product.SupplierRepository;
import com.thatannhien.repository.product.TagRepository;
import com.thatannhien.repository.product.UnitRepository;
import com.thatannhien.repository.product.VariantRepository;
import com.thatannhien.repository.reward.RewardStrategyRepository;
import com.thatannhien.service.CrudService;
import com.thatannhien.service.GenericService;
import com.thatannhien.service.address.ProvinceService;
import com.thatannhien.service.inventory.DocketService;
import com.thatannhien.service.promotion.PromotionService;
import com.thatannhien.service.review.ReviewService;
import com.thatannhien.service.waybill.WaybillService;
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
    }

}
