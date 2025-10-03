package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
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
@Table(name = "product_image")
public class ProductImage {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    @Embedded
    private Audit audit = new Audit();
    private String url;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private WarehouseInventory warehouseInventory;

}
