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
@Table(name = "storeimport")
public class StoreImport {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String description;
    @Embedded
    private Audit audit = new Audit();

    @ManyToMany(mappedBy = "storeImportList")
    private List<Warehouse> warehouseList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "storeid")
    private Store store;

    @OneToMany(mappedBy = "storeImport")
    private List<StoreImportDetail> storeImportDetailList = new ArrayList();

    public void addWarehouse(Warehouse warehouse) {
        warehouse.getStoreImportList().add(this);
        this.getWarehouseList().add(warehouse);
    }

    public void addStore(Store store) {
        store.getStoreImportList().add(this);
        this.setStore(store);
    }


}
