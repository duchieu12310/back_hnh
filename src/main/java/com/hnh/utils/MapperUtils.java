package com.hnh.utils;

import com.hnh.entity.BaseEntity;
import com.hnh.entity.address.Address;
import com.hnh.entity.address.District;
import com.hnh.entity.address.Province;
import com.hnh.entity.address.Ward;
import com.hnh.entity.authentication.User;
import com.hnh.entity.chat.Room;
import com.hnh.entity.customer.Customer;
import com.hnh.entity.customer.CustomerGroup;
import com.hnh.entity.customer.CustomerResource;
import com.hnh.entity.customer.CustomerStatus;
import com.hnh.entity.employee.Department;
import com.hnh.entity.employee.JobLevel;
import com.hnh.entity.employee.JobTitle;
import com.hnh.entity.employee.JobType;
import com.hnh.entity.employee.Office;
import com.hnh.entity.order.Order;
import com.hnh.entity.order.OrderCancellationReason;
import com.hnh.entity.order.OrderResource;
import com.hnh.entity.order.OrderVariantKey;
import com.hnh.entity.product.Brand;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Guarantee;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Supplier;
import com.hnh.entity.product.Unit;
import com.hnh.entity.product.Variant;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.entity.warehouse.Warehouse;
import com.hnh.repository.address.*;
import com.hnh.repository.authentication.*;
import com.hnh.repository.chat.RoomRepository;
import com.hnh.repository.customer.*;
import com.hnh.repository.employee.*;
import com.hnh.repository.order.*;
import com.hnh.repository.product.*;
import com.hnh.repository.warehouse.*;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUtils {

    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private RoleRepository roleRepository;
    @Autowired private TagRepository tagRepository;
    @Autowired private VariantRepository variantRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private ProvinceRepository provinceRepository;
    @Autowired private DistrictRepository districtRepository;
    @Autowired private WardRepository wardRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private AddressRepository addressRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderCancellationReasonRepository orderCancellationReasonRepository;
    @Autowired private WarehouseRepository warehouseRepository;
    @Autowired private StorageLocationRepository storageLocationRepository;
    
    @Autowired private OfficeRepository officeRepository;
    @Autowired private DepartmentRepository departmentRepository;
    @Autowired private JobTypeRepository jobTypeRepository;
    @Autowired private JobLevelRepository jobLevelRepository;
    @Autowired private JobTitleRepository jobTitleRepository;
    
    @Autowired private CustomerGroupRepository customerGroupRepository;
    @Autowired private CustomerResourceRepository customerResourceRepository;
    @Autowired private CustomerStatusRepository customerStatusRepository;
    @Autowired private CustomerRepository customerRepository;
    
    @Autowired private BrandRepository brandRepository;
    @Autowired private SupplierRepository supplierRepository;
    @Autowired private UnitRepository unitRepository;
    @Autowired private GuaranteeRepository guaranteeRepository;
    @Autowired private OrderResourceRepository orderResourceRepository;
    @Autowired private RoomRepository roomRepository;

    @Named("mapToProvince")
    public Province mapToProvince(@Nullable Long id) {
        return id == null ? null : provinceRepository.getById(id);
    }

    @Named("mapToDistrict")
    public District mapToDistrict(@Nullable Long id) {
        return id == null ? null : districtRepository.getById(id);
    }

    @Named("mapToWard")
    public Ward mapToWard(@Nullable Long id) {
        return id == null ? null : wardRepository.getById(id);
    }

    @Named("mapToOffice")
    public Office mapToOffice(Long id) {
        return id == null ? null : officeRepository.getById(id);
    }

    @Named("mapToDepartment")
    public Department mapToDepartment(Long id) {
        return id == null ? null : departmentRepository.getById(id);
    }

    @Named("mapToJobType")
    public JobType mapToJobType(Long id) {
        return id == null ? null : jobTypeRepository.getById(id);
    }

    @Named("mapToJobLevel")
    public JobLevel mapToJobLevel(Long id) {
        return id == null ? null : jobLevelRepository.getById(id);
    }

    @Named("mapToJobTitle")
    public JobTitle mapToJobTitle(Long id) {
        return id == null ? null : jobTitleRepository.getById(id);
    }

    @Named("mapToCustomerGroup")
    public CustomerGroup mapToCustomerGroup(Long id) {
        return id == null ? null : customerGroupRepository.getById(id);
    }

    @Named("mapToCustomerResource")
    public CustomerResource mapToCustomerResource(Long id) {
        return id == null ? null : customerResourceRepository.getById(id);
    }

    @Named("mapToCustomerStatus")
    public CustomerStatus mapToCustomerStatus(Long id) {
        return id == null ? null : customerStatusRepository.getById(id);
    }

    @Named("mapToCategory")
    public Category mapToCategory(Long id) {
        return id == null ? null : categoryRepository.getById(id);
    }
    
    @Named("mapToCategories")
    public Set<Category> mapToCategories(Set<Long> ids) {
        if (ids == null) return null;
        return new HashSet<>(categoryRepository.findAllById(ids));
    }

    @Named("mapToBrand")
    public Brand mapToBrand(Long id) {
        return id == null ? null : brandRepository.getById(id);
    }

    @Named("mapToSupplier")
    public Supplier mapToSupplier(Long id) {
        return id == null ? null : supplierRepository.getById(id);
    }

    @Named("mapToUnit")
    public Unit mapToUnit(Long id) {
        return id == null ? null : unitRepository.getById(id);
    }

    @Named("mapToGuarantee")
    public Guarantee mapToGuarantee(Long id) {
        return id == null ? null : guaranteeRepository.getById(id);
    }

    @Named("mapToOrderResource")
    public OrderResource mapToOrderResource(Long id) {
        return id == null ? null : orderResourceRepository.getById(id);
    }

    @Named("mapToOrderCancellationReason")
    public OrderCancellationReason mapToOrderCancellationReason(Long id) {
        return id == null ? null : orderCancellationReasonRepository.getById(id);
    }

    @Named("mapToCustomer")
    public Customer mapToCustomer(Long id) {
        return id == null ? null : customerRepository.getById(id);
    }

    @Named("mapToOrder")
    public Order mapToOrder(Long id) {
        return id == null ? null : orderRepository.getById(id);
    }

    @Named("mapToRoom")
    public Room mapToRoom(Long id) {
        return id == null ? null : roomRepository.getById(id);
    }
    
    @Named("mapToAddress")
    public Address mapToAddress(Long id) {
        return id == null ? null : addressRepository.getById(id);
    }

    @Named("mapToWarehouse")
    public Warehouse mapToWarehouse(Long id) {
        return id == null ? null : warehouseRepository.getById(id);
    }

    @Named("mapToAddressFromDto")
    public Address mapToAddressFromDto(com.hnh.dto.warehouse.WarehouseRequest.AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setLine(dto.getLine());
        if (dto.getProvince() != null && dto.getProvince().getId() != null) address.setProvince(provinceRepository.getById(dto.getProvince().getId()));
        if (dto.getDistrict() != null && dto.getDistrict().getId() != null) address.setDistrict(districtRepository.getById(dto.getDistrict().getId()));
        if (dto.getWard() != null && dto.getWard().getId() != null) address.setWard(wardRepository.getById(dto.getWard().getId()));
        return addressRepository.save(address);
    }

    @Named("mapToCategoriesFromDto")
    public Set<Category> mapToCategoriesFromDto(java.util.List<com.hnh.dto.warehouse.WarehouseRequest.CategorySelectionDto> dtos) {
        if (dtos == null) return null;
        return new HashSet<>(categoryRepository.findAllById(
            dtos.stream().map(com.hnh.dto.warehouse.WarehouseRequest.CategorySelectionDto::getId).collect(Collectors.toList())
        ));
    }

    @Named("mapToWarehouseFromDto")
    public Warehouse mapToWarehouseFromDto(com.hnh.dto.warehouse.WarehouseRequest.IdDto dto) {
        return dto == null || dto.getId() == null ? null : warehouseRepository.getById(dto.getId());
    }

    @Named("mapToStorageLocation")
    public StorageLocation mapToStorageLocation(Long id) {
        return id == null ? null : storageLocationRepository.getById(id);
    }


    @Named("mapToVariant")
    public Variant mapToVariant(Long id) {
        return id == null ? null : variantRepository.getById(id);
    }

    @Named("mapToProduct")
    public Product mapToProduct(Long id) {
        return id == null ? null : productRepository.getById(id);
    }

    @Named("mapToProducts")
    public Set<Product> mapToProducts(java.util.Collection<Long> productIds) {
        if (productIds == null) return null;
        return productIds.stream()
                .map(this::mapToProduct)
                .collect(Collectors.toSet());
    }

    @Named("mapToUser")
    public User mapToUser(Long id) {
        return id == null ? null : userRepository.getById(id);
    }

    @Named("hashPassword")
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @AfterMapping
    @Named("attachUser")
    public User attachUser(@MappingTarget User user) {
        return user.setRoles(attachSet(user.getRoles(), roleRepository));
    }

    @AfterMapping
    @Named("attachProduct")
    public Product attachProduct(@MappingTarget Product product) {
        if (product.getImages() != null) {
            product.getImages().forEach(image -> image.setProduct(product));
        }
        product.setTags(attachSet(product.getTags(), tagRepository));
        if (product.getVariants() != null) {
            product.getVariants().forEach(variant -> variant.setProduct(product));
        }
        return product;
    }

    @AfterMapping
    @Named("attachOrder")
    public Order attachOrder(@MappingTarget Order order) {
        if (order.getOrderVariants() != null) {
            order.getOrderVariants().forEach(orderVariant -> {
                orderVariant.setOrderVariantKey(new OrderVariantKey(order.getId(), orderVariant.getVariant().getId()));
                orderVariant.setOrder(order);
            });
        }
        return order;
    }

    private <E extends BaseEntity> Set<E> attachSet(Set<E> entities, JpaRepository<E, Long> repository) {
        Set<E> detachedSet = Optional.ofNullable(entities).orElseGet(HashSet::new);
        Set<E> attachedSet = new HashSet<>();

        for (E entity : detachedSet) {
            if (entity.getId() != null) {
                repository.findById(entity.getId()).ifPresent(attachedSet::add);
            } else {
                attachedSet.add(entity);
            }
        }

        return attachedSet;
    }
}
