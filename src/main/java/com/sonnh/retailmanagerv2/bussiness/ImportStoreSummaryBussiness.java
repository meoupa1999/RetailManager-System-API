package com.sonnh.retailmanagerv2.bussiness;

import com.sonnh.retailmanagerv2.data.domain.StoreImportDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ImportStoreSummaryBussiness {
    private Long quantityStoreBefore;
    private Long totalQuantityImport;
    private Long quantityStoreAfter;
    private UUID productId;
    private String productCode;
    private String productName;
    private List<StoreImportDetail> storeImportDetailList;
}
