package com.sonnh.retailmanagerv2.data.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "storeimportdetail")
public class StoreImportDetail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Long quantityWarehouseBefore;
    private Long quantityImport;
    private Long quantityWarehouseAfter;


    //storeimport
    @ManyToOne
    @JoinColumn(name = "storeimport_id")
    private StoreImport storeImport;
    //store_storeinventoiry
    @ManyToOne
    @JoinColumn(name = "store_storeinventory_id")
    private Store_StoreInventory storeStoreInventory;
    //warehouse_warehouseInventory
    @ManyToOne
    @JoinColumn(name = "warehouse_warehouseinventory_id")
    private Warehouse_WarehouseInventory warehouseWarehouseInventory;

    public void addStoreImport(StoreImport storeImport) {
        storeImport.getStoreImportDetailList().add(this);
        this.setStoreImport(storeImport);
    }

    public void addWarehouseWarehouseInventory(Warehouse_WarehouseInventory wwi) {
        wwi.getStoreImportDetailList().add(this);
        this.setWarehouseWarehouseInventory(wwi);
    }

    public void addStoreStoreInventory(Store_StoreInventory ssi) {
        ssi.getStoreImportDetailList().add(this);
        this.setStoreStoreInventory(ssi);
    }

    @ManyToOne
    @JoinColumn(name = "importstoresummaryid")
    private ImportStoreSummary importStoreSummary;
}
