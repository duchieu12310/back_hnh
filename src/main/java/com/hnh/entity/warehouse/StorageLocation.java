package com.hnh.entity.warehouse;

import com.hnh.entity.BaseEntity;
import com.hnh.entity.product.Variant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "storage_location")
public class StorageLocation extends BaseEntity {

    @Column(name = "aisle")
    private String aisle;

    @Column(name = "shelf")
    private String shelf;

    @Column(name = "bin")
    private String bin;

    @OneToMany(mappedBy = "storageLocation", cascade = CascadeType.ALL, orphanRemoval = true)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private java.util.List<InventoryItem> inventoryItems = new java.util.ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Warehouse warehouse;
}

