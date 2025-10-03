package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WarehouseDetailResDto implements Serializable {
    private UUID id;
    private String name;
    private String address;
    private String phone;
    private String email;
    private Double latitude;
    private Double longitude;
    private String description;
    private AuditDto audit;

    public WarehouseDetailResDto(UUID id, String name, String address, String phone, String email, Double latitude, Double longitude, String description, AuditDto audit) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
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
