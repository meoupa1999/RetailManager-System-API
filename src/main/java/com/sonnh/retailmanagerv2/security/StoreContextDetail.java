package com.sonnh.retailmanagerv2.security;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@Data
@NoArgsConstructor
public class StoreContextDetail implements Serializable {
    private UUID storeID;

    public StoreContextDetail(UUID storeID) {
        this.storeID = storeID;
    }
}
