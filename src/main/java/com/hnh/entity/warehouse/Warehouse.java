package com.hnh.entity.warehouse;

import com.hnh.entity.BaseEntity;
import com.hnh.entity.address.Address;
import com.hnh.entity.product.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.util.ArrayList;
import com.hnh.entity.product.Product;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "warehouse")
public class Warehouse extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    public static final Integer STATUS_ACTIVE = 1;      // Hoạt động
    public static final Integer STATUS_MAINTENANCE = 2; // Bảo trì
    public static final Integer STATUS_CLOSED = 3;      // Đóng cửa

    @Column(name = "status", columnDefinition = "TINYINT")
    private Integer status;

    @OneToOne
    @JoinColumn(name = "address_id")
    @NotFound(action = NotFoundAction.IGNORE)
    private Address address;

    @ManyToMany
    @JoinTable(
            name = "warehouse_category",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "warehouse_product",
            joinColumns = @JoinColumn(name = "warehouse_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Set<Product> products = new HashSet<>();


    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<StorageLocation> locations = new ArrayList<>();
}
