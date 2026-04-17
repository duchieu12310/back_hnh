package com.hnh.entity.product;

import com.hnh.entity.BaseEntity;
import com.hnh.entity.client.Preorder;
import com.hnh.entity.client.Wish;
import com.hnh.entity.general.Image;
import com.hnh.entity.promotion.Promotion;
import com.hnh.entity.review.Review;
import com.hnh.utils.JsonNodeConverter;
import com.hnh.entity.warehouse.Warehouse;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "short_description")
    private String shortDescription;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Image> images = new ArrayList<>();

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_category",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    @JsonBackReference
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    @JsonBackReference
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    @JsonBackReference
    private Unit unit;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "product_tag",
            joinColumns = @JoinColumn(name = "product_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id", nullable = false)
    )
    private Set<Tag> tags = new HashSet<>();

    @Column(name = "specifications", columnDefinition = "JSON")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode specifications;

    @Column(name = "properties", columnDefinition = "JSON")
    @Convert(converter = JsonNodeConverter.class)
    private JsonNode properties;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Variant> variants = new ArrayList<>();

    @Column(name = "weight")
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guarantee_id")
    @JsonBackReference
    private Guarantee guarantee;

//    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
//    private ProductInventoryLimit productInventoryLimit;

    @OneToMany(mappedBy = "product")
    private List<Wish> wishes = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Preorder> preorders = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();

    @ManyToMany(mappedBy = "products")
    private Set<Warehouse> warehouses = new HashSet<>();

    @ManyToMany(mappedBy = "products")
    private Set<Promotion> promotions = new HashSet<>();
}

