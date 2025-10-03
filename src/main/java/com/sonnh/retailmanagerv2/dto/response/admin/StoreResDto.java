package com.sonnh.retailmanagerv2.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;
@Data
public class StoreResDto implements Serializable {
    private final UUID id;
    private final String name;
    private final String address;
    private final Double latitude;
    private final Double longitude;
    private final String description;
    private final String province;
    private final String district;
    private final String ward;
    private final String phone;
    private final String mail;
    private final AuditDto audit;

    public StoreResDto(@JsonProperty("id") UUID id, @JsonProperty("name") String name, @JsonProperty("address") String address, @JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude, @JsonProperty("description") String description, @JsonProperty("province") String province, @JsonProperty("district") String district, @JsonProperty("ward") String ward, @JsonProperty("phone") String phone, @JsonProperty("mail") String mail, @JsonProperty("audit") AuditDto audit) {
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
