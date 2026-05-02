package com.hnh.utils;

import com.hnh.entity.BaseEntity;
import com.hnh.entity.address.*;
import com.hnh.entity.authentication.Role;
import com.hnh.entity.authentication.User;
import com.hnh.entity.customer.*;
import com.hnh.entity.employee.*;
import com.hnh.entity.order.*;
import com.hnh.entity.product.*;
import com.hnh.entity.warehouse.*;
import com.hnh.entity.chat.Room;
import com.hnh.dto.warehouse.WarehouseRequest.CategorySelectionDto;
import com.hnh.dto.warehouse.WarehouseRequest.AddressDto;
import com.hnh.dto.authentication.Role_UserRequest;
import com.hnh.repository.address.*;
import com.hnh.repository.authentication.*;
import com.hnh.repository.customer.*;
import com.hnh.repository.employee.*;
import com.hnh.repository.order.*;
import com.hnh.repository.product.*;
import com.hnh.repository.warehouse.*;
import com.hnh.repository.chat.RoomRepository;
import com.hnh.service.geocode.GeocodeService;
import com.hnh.dto.geocode.GeocodeResponse;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapperUtils {

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
    @Autowired private GeocodeService geocodeService;

    @Named("mapToProvince") public Province mapToProvince(@Nullable Long id) { return id == null ? null : provinceRepository.findById(id).orElse(null); }
    @Named("mapToDistrict") public District mapToDistrict(@Nullable Long id) { return id == null ? null : districtRepository.findById(id).orElse(null); }
    @Named("mapToWard") public Ward mapToWard(@Nullable Long id) { return id == null ? null : wardRepository.findById(id).orElse(null); }
    @Named("mapToCategory") public Category mapToCategory(Long id) { return id == null ? null : categoryRepository.findById(id).orElse(null); }
    @Named("mapToBrand") public Brand mapToBrand(Long id) { return id == null ? null : brandRepository.findById(id).orElse(null); }
    @Named("mapToSupplier") public Supplier mapToSupplier(Long id) { return id == null ? null : supplierRepository.findById(id).orElse(null); }
    @Named("mapToUnit") public Unit mapToUnit(Long id) { return id == null ? null : unitRepository.findById(id).orElse(null); }
    @Named("mapToVariant") public Variant mapToVariant(Long id) { return id == null ? null : variantRepository.findById(id).orElse(null); }
    @Named("mapToProduct") public Product mapToProduct(Long id) { return id == null ? null : productRepository.findById(id).orElse(null); }
    @Named("mapToUser") public User mapToUser(Long id) { return id == null ? null : userRepository.findById(id).orElse(null); }
    @Named("mapToOrder") public Order mapToOrder(Long id) { return id == null ? null : orderRepository.findById(id).orElse(null); }
    @Named("mapToWarehouse") public Warehouse mapToWarehouse(Long id) { return id == null ? null : warehouseRepository.findById(id).orElse(null); }
    @Named("mapToStorageLocation") public StorageLocation mapToStorageLocation(Long id) { return id == null ? null : storageLocationRepository.findById(id).orElse(null); }
    @Named("mapToOrderCancellationReason") public OrderCancellationReason mapToOrderCancellationReason(Long id) { return id == null ? null : orderCancellationReasonRepository.findById(id).orElse(null); }
    @Named("mapToOrderResource") public OrderResource mapToOrderResource(Long id) { return id == null ? null : orderResourceRepository.findById(id).orElse(null); }
    @Named("mapToRoom") public Room mapToRoom(Long id) { return id == null ? null : roomRepository.findById(id).orElse(null); }
    @Named("mapToCustomer") public Customer mapToCustomer(Long id) { return id == null ? null : customerRepository.findById(id).orElse(null); }
    @Named("mapToAddress") public Address mapToAddress(Long id) { return id == null ? null : addressRepository.findById(id).orElse(null); }

    @Named("mapToCategories")
    public Set<Category> mapToCategories(Set<Long> ids) {
        if (ids == null) return null;
        return new HashSet<>(categoryRepository.findAllById(ids));
    }

    @Named("hashPassword")
    public String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Named("attachUserRoles")
    public Set<Role> attachUserRoles(Set<Role_UserRequest> roles) {
        if (roles == null) return new HashSet<>();
        return roles.stream()
                .filter(r -> r.getId() != null)
                .map(r -> roleRepository.findById(r.getId()).orElse(null))
                .filter(java.util.Objects::nonNull)
                .collect(Collectors.toSet());
    }

    @Named("mapToAddressFromDto")
    public Address mapToAddressFromDto(AddressDto dto) {
        if (dto == null) return null;
        Address address = new Address();
        address.setLine(dto.getLine());
        Long provId = (dto.getProvince() != null) ? dto.getProvince().getId() : null;
        Long distId = (dto.getDistrict() != null) ? dto.getDistrict().getId() : null;
        Long wardId = (dto.getWard() != null) ? dto.getWard().getId() : null;
        if (provId != null) address.setProvince(provinceRepository.findById(provId).orElse(null));
        if (distId != null) address.setDistrict(districtRepository.findById(distId).orElse(null));
        if (wardId != null) address.setWard(wardRepository.findById(wardId).orElse(null));

        // Tự động lấy tọa độ
        GeocodeResponse coords = geocodeService.getCoordinates(provId, distId, wardId);
        if (coords != null) {
            address.setLatitude(coords.getLatitude());
            address.setLongitude(coords.getLongitude());
        }

        return addressRepository.save(address);
    }

    @Named("mapToCategoriesFromDto")
    public Set<Category> mapToCategoriesFromDto(java.util.List<CategorySelectionDto> dtos) {
        if (dtos == null) return null;
        return new HashSet<>(categoryRepository.findAllById(dtos.stream().map(CategorySelectionDto::getId).collect(Collectors.toList())));
    }

    protected <E extends BaseEntity> Set<E> attachSet(Set<E> entities, JpaRepository<E, Long> repository) {
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

    @Named("mapToOffice") public Office mapToOffice(Long id) { return id == null ? null : officeRepository.findById(id).orElse(null); }
    @Named("mapToDepartment") public Department mapToDepartment(Long id) { return id == null ? null : departmentRepository.findById(id).orElse(null); }
    @Named("mapToJobType") public JobType mapToJobType(Long id) { return id == null ? null : jobTypeRepository.findById(id).orElse(null); }
    @Named("mapToJobLevel") public JobLevel mapToJobLevel(Long id) { return id == null ? null : jobLevelRepository.findById(id).orElse(null); }
    @Named("mapToJobTitle") public JobTitle mapToJobTitle(Long id) { return id == null ? null : jobTitleRepository.findById(id).orElse(null); }
    @Named("mapToCustomerGroup") public CustomerGroup mapToCustomerGroup(Long id) { return id == null ? null : customerGroupRepository.findById(id).orElse(null); }
    @Named("mapToCustomerResource") public CustomerResource mapToCustomerResource(Long id) { return id == null ? null : customerResourceRepository.findById(id).orElse(null); }
    @Named("mapToCustomerStatus") public CustomerStatus mapToCustomerStatus(Long id) { return id == null ? null : customerStatusRepository.findById(id).orElse(null); }
    @Named("mapToGuarantee") public Guarantee mapToGuarantee(Long id) { return id == null ? null : guaranteeRepository.findById(id).orElse(null); }
    @Named("mapToProducts") public Set<Product> mapToProducts(java.util.Collection<Long> productIds) { if (productIds == null) return null; return productIds.stream().map(id -> productRepository.findById(id).orElse(null)).collect(Collectors.toSet()); }
}
