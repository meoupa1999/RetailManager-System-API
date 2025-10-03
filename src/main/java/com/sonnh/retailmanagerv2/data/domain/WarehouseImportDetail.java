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
@Table(
        name = "warehouseimportdetail"
)
public class WarehouseImportDetail {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Long quantityBefore;
    private Long quantityImport;
    private Long quantityAfter;
    @ManyToOne
    @JoinColumn(name = "warehouseimport_id")
    private WarehouseImport warehouseImport;
    @ManyToOne
    @JoinColumn(name = "warehouse_warehouseinventory_id")
    private Warehouse_WarehouseInventory warehouseWarehouseInventory;

    public void addWarehouseImport(WarehouseImport warehouseImport) {
        warehouseImport.getWarehouseImportDetailList().add(this);
        this.setWarehouseImport(warehouseImport);
    }

    public void addWarehouseWarehouseInventory(Warehouse_WarehouseInventory wwi) {
        wwi.getWarehouseImportDetailList().add(this);
        this.setWarehouseWarehouseInventory(wwi);
    }
}
