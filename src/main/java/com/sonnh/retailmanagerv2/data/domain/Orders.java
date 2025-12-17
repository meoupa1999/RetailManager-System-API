package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import com.sonnh.retailmanagerv2.data.domain.enums.OrderStatus;
import com.sonnh.retailmanagerv2.data.domain.enums.OrderType;
import com.sonnh.retailmanagerv2.data.domain.enums.PaymentMethod;
import com.sonnh.retailmanagerv2.data.domain.enums.RankStatus;
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
@Table(name = "orders"
)
public class Orders {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String shipAddress;
    private String shipPhone;
    private Long totalPriceBeforeDiscount;
//    private Double totalPriceAfterDiscount;
    private Long totalDiscountProduct;
    private Long totalDiscountRank;
    private Long finalPrice;
    @Enumerated(EnumType.STRING)
    private OrderType orderType;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
//    private Integer pointEarned;
    private String description;
    @Enumerated(EnumType.STRING)
    private RankStatus rankApplied;
    private OrderStatus status;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Account customer;
    @OneToMany(mappedBy = "order")
    private List<OrderDetail> orderDetailList = new ArrayList();

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    public void addStore(Store store) {
        store.getOrdersList().add(this);
        this.setStore(store);
    }
}

