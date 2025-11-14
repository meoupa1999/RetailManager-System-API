package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "storeinventory")
public class StoreInventory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String productCode;
    private String name;
    private String brand;
    private String description;
    @Embedded
    private Audit audit = new Audit();
    @OneToMany(mappedBy = "storeInventory")
    private List<Store_StoreInventory> store_storeInventoryList = new ArrayList();
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;


    @OneToMany(mappedBy = "product")
    private List<Guaranted> guarantedList = new ArrayList();


    public void addCategory(Category category) {
        category.getStoreInventoryList().add(this);
        this.setCategory(category);
    }


}
