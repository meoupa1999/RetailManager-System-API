package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class StoreDetailResDto implements Serializable {
    private UUID id;
    private String name;
    private String address;
    private Double latitude;
    private Double longitude;
    private String description;
    private String province;
    private String district;
    private String ward;
    private String phone;
    private String mail;
    private AuditDto audit;

    public StoreDetailResDto(UUID id, String name, String address, Double latitude, Double longitude, String description, String province, String district, String ward, String phone, String mail, AuditDto audit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.phone = phone;
        this.mail = mail;
        this.audit = audit;
    }

    @Data
    public static class AuditDto implements Serializable {
        String createdBy;
        LocalDateTime createdAt;
        String updatedBy;
        LocalDateTime updatedAt;
        Boolean isActive;
    }
}
