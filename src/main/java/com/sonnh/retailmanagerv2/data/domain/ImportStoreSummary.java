package com.sonnh.retailmanagerv2.data.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.awt.print.Book;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "importstoresummary")
public class ImportStoreSummary {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;

    private Long quantityStoreAfter;

    private Long quantityStoreBefore;

    private Long totalQuantityImport;

    private String productCode;

    private String productName;
    //productId
    private UUID productId;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;


    // store import
    private UUID storeImportId;
    //storeimport detail
    @OneToMany(mappedBy = "importStoreSummary")
    private List<StoreImportDetail> storeImportDetailList = new ArrayList<>();

    public void addStore(Store store) {
        store.getImportStoreSummaryList().add(this);
        this.setStore(store);
    }

    public void addStoreImportDetails(List<StoreImportDetail> storeImportDetailList) {
        storeImportDetailList.stream().forEach(storeImportDetail -> storeImportDetail.setImportStoreSummary(this));
        this.setStoreImportDetailList(storeImportDetailList);
    }

}
