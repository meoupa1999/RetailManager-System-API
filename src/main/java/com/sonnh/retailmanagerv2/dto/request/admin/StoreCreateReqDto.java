package com.sonnh.retailmanagerv2.dto.request.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class StoreCreateReqDto implements Serializable {
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

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public StoreCreateReqDto(@JsonProperty("name") String name,
                             @JsonProperty("address") String address,
                             @JsonProperty("latitude") Double latitude,
                             @JsonProperty("longitude") Double longitude,
                             @JsonProperty("description") String description,
                             @JsonProperty("province") String province,
                             @JsonProperty("district") String district,
                             @JsonProperty("ward") String ward,
                             @JsonProperty("phone") String phone,
                             @JsonProperty("mail") String mail) {
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
