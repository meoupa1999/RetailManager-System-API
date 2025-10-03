package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;

import java.io.Serializable;
@Data
public class WarehouseUpdateReqDto implements Serializable {
    private String name;
    private String address;
    private String phone;
    private String email;
    private Double latitude;
    private Double longitude;
    private String description;

    public WarehouseUpdateReqDto(String name, String address, String phone, String email, Double latitude, Double longitude, String description) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
    }

}
