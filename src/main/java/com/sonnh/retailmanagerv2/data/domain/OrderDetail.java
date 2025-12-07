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
@Table(name = "order_detail")
public class OrderDetail { @Id @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Long quantity;
    private Long unitPrice;
    private Long originalPrice;
    private Long discountAmount;
    private Long totalPrice;
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Orders order;
    @OneToMany(mappedBy = "orderDetail")
    private List<Guaranted> guarantedList = new ArrayList();
}
