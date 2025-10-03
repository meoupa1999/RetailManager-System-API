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
@Table(name = "warehouse")
public class Warehouse {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Double latitude;
    private Double longitude;
    private String address;
    private String description;
    private String name;
    private String email;
    private String phone;
    @Embedded
    private Audit audit = new Audit();

    @OneToMany(mappedBy = "warehouse")
    private List<Warehouse_WarehouseInventory> warehouseWarehouseInventoryList  = new ArrayList<>();

    @OneToMany(mappedBy = "warehouse")
    private List<WarehouseImport> warehouseImportList = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "warehouse_storeimport",
            joinColumns = @JoinColumn(name = "warehouseid"),
            inverseJoinColumns = @JoinColumn(name = "storeimportid"))
    private List<StoreImport> storeImportList = new ArrayList<>();


}
