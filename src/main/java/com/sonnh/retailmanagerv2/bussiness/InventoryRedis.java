package com.sonnh.retailmanagerv2.bussiness;

import com.sonnh.retailmanagerv2.data.domain.Promotion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryRedis implements Serializable {
    private UUID productId;
    private Long quantity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InventoryRedis)) return false;
        InventoryRedis inventoryRedis = (InventoryRedis) o;
        if (this.productId == null || inventoryRedis.productId == null) {
            return false;
        }
        return this.productId.equals(inventoryRedis.productId);
    }

    @Override
    public int hashCode() {
        return (productId == null) ? System.identityHashCode(this) : productId.hashCode();
    }

    public boolean containsProductId(UUID productId) {
        if (this.getProductId().equals(productId))
            return true;
        return false;
    }
}
