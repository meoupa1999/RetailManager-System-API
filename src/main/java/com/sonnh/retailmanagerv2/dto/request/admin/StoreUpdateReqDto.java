package com.sonnh.retailmanagerv2.dto.request.admin;

import lombok.Data;

import java.io.Serializable;
@Data
public class StoreUpdateReqDto implements Serializable {
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

    public StoreUpdateReqDto(String name, String address, Double latitude, Double longitude, String description, String province, String district, String ward, String phone, String mail) {
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
    }
}
