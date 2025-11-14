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
@Table(
        name = "store_storeinventory"
)
public class Store_StoreInventory {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Long quantity;
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
    @ManyToOne
    @JoinColumn(name = "storeinventory_id")
    private StoreInventory storeInventory;

    @OneToMany(mappedBy = "storeStoreInventory")
    private List<StoreImportDetail> storeImportDetailList = new ArrayList();


    @ManyToMany
    @JoinTable(
            name = "promotion_store_storeinventory",
            joinColumns = @JoinColumn(name = "store_storeinventory_id"),
            inverseJoinColumns = @JoinColumn(name = "promotion_id")
    )
    private List<Promotion> promotionList = new ArrayList<>();
    public void addStore(Store store) {
        store.getStoreStoreInventoryList().add(this);
        this.setStore(store);
    }

    public void addStoreInventory(StoreInventory storeInventory) {
        storeInventory.getStore_storeInventoryList().add(this);
        this.setStoreInventory(storeInventory);
    }

    public void addPromotion(Promotion promotion) {
        promotion.getStore_storeInventoryList().add(this);
        this.getPromotionList().add(promotion);
    }
}
