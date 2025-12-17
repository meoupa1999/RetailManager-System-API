package com.sonnh.retailmanagerv2.dto.response.admin;

import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link com.sonnh.retailmanagerv2.data.domain.Store}
 */
@Data
public class StoreByProductResDto implements Serializable {
    private UUID id;
    private String address;
    private String district;
    private String mail;
    private String name;
    private String phone;
    private String province;
    private String ward;

    public StoreByProductResDto(UUID id,
                                String address,
                                String district,
                                String mail,
                                String name,
                                String phone,
                                String province,
                                String ward) {
        this.id = id;
        this.address = address;
        this.district = district;
        this.mail = mail;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.ward = ward;
    }
}