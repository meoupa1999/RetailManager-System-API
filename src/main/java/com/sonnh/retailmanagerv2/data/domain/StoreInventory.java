package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.*;

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

    @ManyToMany
    @JoinTable(
            name = "promotion_storeinventory",
            joinColumns = @JoinColumn(name = "storeiventory_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private Set<Promotion> promotionList = new HashSet<>();

    public void addCategory(Category category) {
        category.getStoreInventoryList().add(this);
        this.setCategory(category);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StoreInventory)) return false;

        StoreInventory storeInventory = (StoreInventory) o;

        if (this.id == null || storeInventory.id == null) {
            return false;
        }

        return this.id.equals(storeInventory.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? System.identityHashCode(this) : id.hashCode();
    }
}
