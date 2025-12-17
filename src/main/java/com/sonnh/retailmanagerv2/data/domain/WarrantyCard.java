package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.enums.GuarantedStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@EntityListeners({AuditingEntityListener.class})
@Table(name = "warrantycard")
public class WarrantyCard {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String description;
    private LocalDateTime startWarrantyTime;
    private LocalDateTime completeWarrantyTime;
    private GuarantedStatus status;


    @ManyToOne
    @JoinColumn(name = "orderdetail_id")
    private OrderDetail orderDetail;

    @ManyToOne
    @JoinColumn(name = "guaranted_id")
    private Guaranted guaranted;

    public void addOrderDetail(OrderDetail orderDetail) {
        orderDetail.getWarrantyCardList().add(this);
        this.setOrderDetail(orderDetail);
    }
    public void addGuaranted(Guaranted guaranted) {
        guaranted.getWarrantyCardList().add(this);
        this.setGuaranted(guaranted);
    }
}
