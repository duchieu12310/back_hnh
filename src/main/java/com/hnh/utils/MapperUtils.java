package com.hnh.utils;

import com.hnh.entity.BaseEntity;
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
import com.hnh.repository.address.DistrictRepository;
import com.hnh.repository.address.ProvinceRepository;
import com.hnh.repository.address.WardRepository;
import com.hnh.repository.authentication.RoleRepository;
import com.hnh.repository.authentication.UserRepository;
import com.hnh.repository.product.ProductRepository;
import com.hnh.repository.product.TagRepository;
import com.hnh.repository.product.VariantRepository;
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

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class MapperUtils {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private VariantRepository variantRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProvinceRepository provinceRepository;
    @Autowired
    private DistrictRepository districtRepository;
    @Autowired
    private WardRepository wardRepository;
    @Autowired
    private UserRepository userRepository;

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
    public abstract Office mapToOffice(Long id);

    @Named("mapToDepartment")
    public abstract Department mapToDepartment(Long id);

    @Named("mapToJobType")
    public abstract JobType mapToJobType(Long id);

    @Named("mapToJobLevel")
    public abstract JobLevel mapToJobLevel(Long id);

    @Named("mapToJobTitle")
    public abstract JobTitle mapToJobTitle(Long id);

    @Named("mapToCustomerGroup")
    public abstract CustomerGroup mapToCustomerGroup(Long id);

    @Named("mapToCustomerResource")
    public abstract CustomerResource mapToCustomerResource(Long id);

    @Named("mapToCustomerStatus")
    public abstract CustomerStatus mapToCustomerStatus(Long id);

    @Named("mapToCategory")
    public abstract Category mapToCategory(Long id);

    @Named("mapToBrand")
    public abstract Brand mapToBrand(Long id);

    @Named("mapToSupplier")
    public abstract Supplier mapToSupplier(Long id);

    @Named("mapToUnit")
    public abstract Unit mapToUnit(Long id);

    @Named("mapToGuarantee")
    public abstract Guarantee mapToGuarantee(Long id);

    @Named("mapToOrderResource")
    public abstract OrderResource mapToOrderResource(Long id);

    @Named("mapToOrderCancellationReason")
    public abstract OrderCancellationReason mapToOrderCancellationReason(Long id);

    @Named("mapToCustomer")
    public abstract Customer mapToCustomer(Long id);

    @Named("mapToOrder")
    public abstract Order mapToOrder(Long id);

    @Named("mapToRoom")
    public abstract Room mapToRoom(Long id);

    @Named("mapToVariant")
    public Variant mapToVariant(Long id) {
        return variantRepository.getById(id);
    }

    @Named("mapToProduct")
    public Product mapToProduct(Long id) {
        return productRepository.getById(id);
    }

    @Named("mapToProducts")
    public Set<Product> mapToProducts(Set<Long> productIds) {
        if (productIds == null) {
            return null;
        }
        return productIds.stream()
                .map(this::mapToProduct)
                .collect(java.util.stream.Collectors.toSet());
    }

    @Named("mapToUser")
    public User mapToUser(Long id) {
        return userRepository.getById(id);
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
        product.getImages().forEach(image -> image.setProduct(product));
        product.setTags(attachSet(product.getTags(), tagRepository));
        product.getVariants().forEach(variant -> variant.setProduct(product));
        return product;
    }
    @AfterMapping
    @Named("attachOrder")
    public Order attachOrder(@MappingTarget Order order) {
        order.getOrderVariants().forEach(orderVariant -> {
            orderVariant.setOrderVariantKey(new OrderVariantKey(order.getId(), orderVariant.getVariant().getId()));
            orderVariant.setOrder(order);
        });
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
