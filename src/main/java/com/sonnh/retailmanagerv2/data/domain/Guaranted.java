package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.stat.descriptive.summary.Product;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "guaranted")
public class Guaranted {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private Integer durationTime;
    private LocalDateTime startDate;
    private LocalDateTime expiredDate;
    @Enumerated(EnumType.STRING)
    private GuarantedStatus guarantedStatus;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account customer;
    //    @ManyToOne
//    @JoinColumn(name = "storeinventory_id")
//    private StoreInventory product;
//    @ManyToOne
//    @JoinColumn(name = "orderDetail_id")
//    private OrderDetail orderDetail;

    @OneToMany(mappedBy = "guaranted")
    private List<WarrantyCard> warrantyCardList = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "orderdetail_id")  // Khóa ngoại tham chiếu đến bảng User
    private OrderDetail orderDetail;

    public void addCustomer (Account cus) {
        cus.getGuarantedList().add(this);
        this.setCustomer(cus);
    }

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetail.setGuaranted(this);
        this.setOrderDetail(orderDetail);
    }
}
