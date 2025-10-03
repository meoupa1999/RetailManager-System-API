package com.sonnh.retailmanagerv2.dto.response.admin;

import com.sonnh.retailmanagerv2.data.domain.*;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * DTO for {@link Store}
 */
@Data
public class ImportStoreReportResDto implements Serializable {
    private UUID id;
    private String name;
    private StoreImportDto storeImportDto;

    /**
     * DTO for {@link StoreImport}
     */
    @Data
    public static class StoreImportDto implements Serializable {
        UUID id;
        AuditDto audit;
        List<ImportStoreSummaryDto> importStoreSummaryDtoList; // done

        /**
         * DTO for {@link com.sonnh.retailmanagerv2.data.domain.embedded.Audit}
         */
        @Data
        public static class AuditDto implements Serializable {
            String createdBy;
            LocalDateTime createdAt;
            String updatedBy;
            LocalDateTime updatedAt;
        }

        /**
         * DTO for {@link StoreImportDetail}
         */
        @Data
        public static class ImportStoreSummaryDto implements Serializable {
            UUID id;
            String productCode;
            String productName;
            Long quantityStoreBefore;
            Long quantityStoreAfter;
            List<StoreImportDetailDto> storeImportDetailDtoList; //done


            /**
             * DTO for {@link Warehouse_WarehouseInventory}
             */
            @Data
            public static class StoreImportDetailDto implements Serializable {
                UUID id;
                Warehouse_WarehouseInventoryDto warehouseWarehouseInventoryDto;  //done

                @Data
                public static class Warehouse_WarehouseInventoryDto implements Serializable {
                    UUID id;
                    WarehouseDto warehouseDto;   //here

                    /**
                     * DTO for {@link com.sonnh.retailmanagerv2.data.domain.embedded.Audit}
                     */

                    @Data
                    public static class WarehouseDto implements Serializable {
                        private UUID id;
                        private String name;
                        Long quantityWarehouseBefore;
                        Long quantityImport;
                        Long quantityWarehouseAfter;
                    }

                }
            }

            /**
             * DTO for {@link ImportStoreSummary}
             */

        }
    }
}