package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "warehouseinventory")
public class WarehouseInventory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String productCode;
    private String name;
    private String description;
    private String suplier;
    private String unitOfMeasure;
    private Double purchasePrice;
    private Double salePrice;
    private Integer warrantyPeriod;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(mappedBy = "warehouseInventory")
    private List<Warehouse_WarehouseInventory> warehouse_warehouseInventoryList = new ArrayList();

//    public UUID getId() {
//        return id;
//    }
//
//    public List<Warehouse_WarehouseInventory> getWarehouse_warehouseInventoryList() {
//        return warehouse_warehouseInventoryList;
//    }

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "warehouseInventory")
    private List<ProductImage> productImageList = new ArrayList();

    public void addImages(List<ProductImage> images) {
        images.stream().forEach(i -> i.setWarehouseInventory(this));
        this.setProductImageList(images);
    }

}
