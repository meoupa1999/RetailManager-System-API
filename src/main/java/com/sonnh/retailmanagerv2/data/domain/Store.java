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
@Table(name = "store")
public class Store {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private String district;
    private String mail;
    private String name;
    private String phone;
    private String province;
    private String ward;
    @Embedded
    private Audit audit = new Audit();

    @OneToMany(mappedBy = "store")
    private List<Store_StoreInventory> storeStoreInventoryList = new ArrayList();

    @OneToMany(mappedBy = "store")
    private List<StoreImport> storeImportList = new ArrayList();

    @OneToMany(mappedBy = "store")
    private List<ImportStoreSummary> importStoreSummaryList = new ArrayList<>();

    @OneToMany(mappedBy = "store")
    private List<Account> empoyeeAccountList = new ArrayList<>();


}
