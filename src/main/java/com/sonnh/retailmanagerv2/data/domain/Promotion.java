package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import com.sonnh.retailmanagerv2.data.domain.enums.PromotionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "promotion")
public class Promotion {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String name;
    private Double discountPercent;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    @Enumerated(EnumType.STRING)
    private PromotionStatus status;
    @Embedded
    private Audit audit = new Audit();

    @ManyToMany(mappedBy = "promotionList")
    private List<Store_StoreInventory> store_storeInventoryList = new ArrayList<>();

    @ManyToMany(mappedBy = "promotionList")
    private Set<StoreInventory> storeInventoryList = new HashSet<>();



    public void addProduct(StoreInventory product) {
        product.getPromotionList().add(this);
        this.getStoreInventoryList().add(product);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Promotion)) return false;
        Promotion promotion = (Promotion) o;
        if (this.id == null || promotion.id == null) {
            return false;
        }
        return this.id.equals(promotion.id);
    }

    @Override
    public int hashCode() {
        return (id == null) ? System.identityHashCode(this) : id.hashCode();
    }

}
