package com.sonnh.retailmanagerv2.data.domain;

import com.sonnh.retailmanagerv2.data.domain.embedded.Audit;
import com.sonnh.retailmanagerv2.data.domain.enums.RankStatus;
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
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(nullable = false, columnDefinition = "uniqueIdentifier")
    private UUID id;
    private String mail;
    private String phone;
    private String username;
    private String password;
    private String name;
    private String address;
    private String note;
    private long loyaltyPoint;
//    @Enumerated(EnumType.STRING)
//    private RankStatus rankStatus;
    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank rank;

    private Double latitude;
    private Double longgitude;
    @Embedded
    private Audit audit = new Audit();
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "customerRank_id")
    private CustomerRank customerRank;

    @OneToMany(mappedBy = "customer")
    private List<Guaranted> guarantedList = new ArrayList();

    @OneToMany(mappedBy = "customer")
    private Set<Orders> ordersSet = new HashSet();
}