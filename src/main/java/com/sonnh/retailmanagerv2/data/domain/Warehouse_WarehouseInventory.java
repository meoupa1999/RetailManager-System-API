package com.sonnh.retailmanagerv2.data.domain;

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
@Table(name = "warehouse_warehouseinventory")
public class Warehouse_WarehouseInventory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private long quantity;
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;
    @ManyToOne
    @JoinColumn(name = "warehouseinventory_id")
    private WarehouseInventory warehouseInventory;
    @OneToMany(mappedBy = "warehouseWarehouseInventory")
    private List<WarehouseImportDetail> warehouseImportDetailList = new ArrayList();

    @OneToMany(mappedBy = "warehouseWarehouseInventory")
    private List<StoreImportDetail> storeImportDetailList = new ArrayList();

    public void addWarehouse(Warehouse warehouse) {
        warehouse.getWarehouseWarehouseInventoryList().add(this);
        this.setWarehouse(warehouse);
    }

    public void addProduct(WarehouseInventory product) {
        product.getWarehouse_warehouseInventoryList().add(this);
        this.setWarehouseInventory(product);
    }
}
