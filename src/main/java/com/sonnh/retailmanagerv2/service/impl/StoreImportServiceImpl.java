package com.sonnh.retailmanagerv2.service.impl;

import com.sonnh.retailmanagerv2.bussiness.ImportStoreSummaryBussiness;
import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.repository.*;
import com.sonnh.retailmanagerv2.dto.request.admin.ImportStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.SuggestWarehouseReqDto;
import com.sonnh.retailmanagerv2.dto.response.SuggestWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ImportStoreReportResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.StoreResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryExcelResDto;
import com.sonnh.retailmanagerv2.exception.store_exception.StoreNotFoundException;
import com.sonnh.retailmanagerv2.service.interfaces.StoreImportService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreImportServiceImpl implements StoreImportService {
    private static final double EARTH_RADIUS_KM = 6371.0; // Bán kính Trái Đất (km)
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final StoreRepository storeRepository;
    private final StoreImportRepository storeImportRepository;
    private final StoreInventoryRepository storeInventoryRepository;
    private final Warehouse_WarehouseInventoryRepository warehouseWarehouseInventoryRepository;
    private final Store_StoreInventoryRepository storeStoreInventoryRepository;
    private final StoreImportDetailRepository storeImportDetailRepository;
    private final ImportStoreSummaryRepository importStoreSummaryRepository;


    @Override
    @Transactional
    public List<SuggestWarehouseResDto> suggestWarehouse(List<SuggestWarehouseReqDto> dtoList, UUID storeId) {
        List<SuggestWarehouseResDto> suggestWarehouseResDtoList = new ArrayList<>();
        Long total = 0L;
        Double supplyScore = 0.0;
        Double weight;
        Double supplyRatio;
        Double distance;
        Double distanceScore;
        Double warehouseLat;
        Double warehouseLon;
        Double storeLat;
        Double storeLon;
        Double totalScore;
        // map<id của warehouse,map<id của product,quantity>>
        Map<UUID, Map<UUID, Long>> map = new HashMap<>();
        //lấy ra list all warehouse
        List<Warehouse> warehouseList = warehouseRepository.findAll().stream().collect(Collectors.toList());
        System.out.println("size : " + warehouseList.size());
        for (Warehouse keyWarehouse : warehouseList) {
            //cái map value của "map": Map<UUID, Long>
            Map<UUID, Long> productMap = new HashMap<>();
            for (Warehouse_WarehouseInventory keyWarehouseWarehouseInventory : keyWarehouse.getWarehouseWarehouseInventoryList()) {
                //put vô productMap key là id của product và value là quantity của product đó tại warehouse đó
                productMap.put(keyWarehouseWarehouseInventory.getWarehouseInventory().getId(), keyWarehouseWarehouseInventory.getQuantity());
            }
            // put key là warehouseId vô "map" và put nguyên cái productMap làm value của "map"
            map.put(keyWarehouse.getId(), productMap);
        }
        for (UUID key1 : map.keySet()) {
            System.out.println("key ngoài : " + key1);
            for (UUID key2 : map.get(key1).keySet()) {
                System.out.println(" key trong: " + key2 + " value trong: " + map.get(key1).get(key2));
            }
        }
        // lấy tổng product store cần ra
        for (SuggestWarehouseReqDto value : dtoList) {
            total += value.getQuantity();
        }
        System.out.println("total: " + total);
        //lấy store ra
        Store store = storeRepository.findById(storeId).orElseThrow(handlerStoreNotFound);

        for (UUID key : map.keySet()) {
            UUID warehouseId = key;
            System.out.println("WarehouseId: " + key);
            for (SuggestWarehouseReqDto value : dtoList) {
//                    System.out.println("hêlo");
                if (map.get(key).get(value.getId()) != null) {
                    System.out.print("productId: " + value.getId());
                    System.out.print("--quantity ne " + value.getQuantity());
                    System.out.println("tính nè: " + ((double) value.getQuantity() / total) * ((double) map.get(key).get(value.getId()) / value.getQuantity()));
                    weight = (double) value.getQuantity() / total;
                    supplyRatio = (double) map.get(key).get(value.getId()) / value.getQuantity();
                    if (supplyRatio > 1.0)
                        supplyRatio = 1.0;
                    supplyScore += weight * supplyRatio;
                }
            }
            warehouseLat = warehouseRepository.findById(key).get().getLatitude();
            warehouseLon = warehouseRepository.findById(key).get().getLongitude();
            storeLat = store.getLatitude();
            storeLon = store.getLongitude();
            distance = haversine(warehouseLat, warehouseLon, storeLat, storeLon);
            System.out.println("distance: " + distance);
            distanceScore = 1 / (1 + (distance / 10));
            System.out.println("distanceScore: " + distanceScore);
            totalScore = (supplyScore + distanceScore) / 2;
            System.out.println("total score: " + totalScore);
            suggestWarehouseResDtoList.add(new SuggestWarehouseResDto(warehouseId, totalScore * 100));
            supplyScore = 0.0;
        }

        return suggestWarehouseResDtoList;
    }

    @Override
    public XSSFWorkbook exportExcelImportStore(ImportStoreReqDto dto) {
        return createExcelFile(dto);
    }

    @Override
    @Transactional
    public void importProductForStore(UUID storeId, MultipartFile file) {
        Map<UUID, ImportStoreSummaryBussiness> importSummaryMap = new HashMap<>();
        // UUID: warehouseId,UUID:productId trong file excel,Long quantity của product trong file excel
        Map<UUID, Map<UUID, Long>> warehouseMap = readImportStoreExcelFile(file);
        //new StoreImport
        StoreImport storeImport = new StoreImport();
        //get Store by id
        Store store = storeRepository.findById(storeId).get();
        //add store vô storeimport
        storeImport.addStore(store);
        Warehouse warehouse;
        for (UUID warehouseId : warehouseMap.keySet()) {
            //lấy warehouse từ map ra
            warehouse = warehouseRepository.findById(warehouseId).get();
            //add warehouse vô storeImport
            storeImport.addWarehouse(warehouse);
        }
        //save vô db storeimport trước
        storeImportRepository.save(storeImport);
//        Map<UUID, Long> productMap = warehouseMap.
        // vòng for duyệt key của warehouseMap
        for (UUID warehouseId : warehouseMap.keySet()) {
            //vòng for duyệt value của warehouseMap tức là Map<UUID,Long> id của product và quantity
            for (UUID productId : warehouseMap.get(warehouseId).keySet()) {
                if (warehouseMap.get(warehouseId).get(productId) > 0) {
                    StoreImportDetail storeImportDetail = new StoreImportDetail();
                    storeImportDetail.addStoreImport(storeImport);
                    Warehouse_WarehouseInventory wwi = warehouseWarehouseInventoryRepository
                            .findWarehouse_WarehouseInventories(warehouseId, productId);
                    WarehouseInventory productWarehouse = warehouseInventoryRepository.findById(productId).get();
                    Optional<StoreInventory> storeProductOp = storeInventoryRepository.findByProductCode(productWarehouse.getProductCode());
                    // case product đã có trong storeinventory
                    if (storeProductOp.isPresent()) {
                        Store_StoreInventory ssi;
                        System.out.println("chay vo day 1");
                        StoreInventory storeProduct = storeProductOp.get();
                        Store_StoreInventory ssiE = storeStoreInventoryRepository.findStoreStoreInventory(storeId, storeProduct.getId());
                       //case product chưa connect với store thông qua store_storeinventory
                        if (Objects.isNull(ssiE)) {
                            //tạo mới một store_storeinventory
                            ssi = new Store_StoreInventory();
                            ssi.setStore(store);
                            ssi.addStoreInventory(storeProduct);
                            ssi.setQuantity(0L);
                            storeStoreInventoryRepository.save(ssi);
                            //
                            System.out.println("save ssi thanh cong");
                            //case product đã connect với store thông qua store_storeinventory
                        } else
                            ssi = ssiE;
                        //
                        // go block
                        if (importSummaryMap.containsKey(productId)) {
                           ImportStoreSummaryBussiness importStoreSummaryBussiness = importSummaryMap.get(productId);
                           importStoreSummaryBussiness.setTotalQuantityImport(importStoreSummaryBussiness.getTotalQuantityImport() + warehouseMap.get(warehouseId).get(productId));
                           importStoreSummaryBussiness.setQuantityStoreAfter(importStoreSummaryBussiness.getQuantityStoreAfter() + warehouseMap.get(warehouseId).get(productId));
                           importSummaryMap.put(productId,importStoreSummaryBussiness);
                        }else {
                            ImportStoreSummaryBussiness importStoreSummaryBussiness = new ImportStoreSummaryBussiness();
                            importStoreSummaryBussiness.setQuantityStoreBefore(ssi.getQuantity());
                            importStoreSummaryBussiness.setTotalQuantityImport(warehouseMap.get(warehouseId).get(productId));
                            importStoreSummaryBussiness.setProductId(productId);
                            importStoreSummaryBussiness.setProductCode(productWarehouse.getProductCode());
                            importStoreSummaryBussiness.setProductName(productWarehouse.getName());
                            importStoreSummaryBussiness.setQuantityStoreAfter(ssi.getQuantity() + warehouseMap.get(warehouseId).get(productId));
                            importStoreSummaryBussiness.getStoreImportDetailList().add(storeImportDetail);
                            importSummaryMap.put(productId,importStoreSummaryBussiness);
                        }

                        //
                        Long quantityWarehouseBefore = wwi.getQuantity();
                        storeImportDetail.setQuantityWarehouseBefore(quantityWarehouseBefore);
//                       Long quantityStoreBefore = ssi.getQuantity();
//                       System.out.println("Can tim 1: " + quantityStoreBefore);
//                       storeImportDetail.setQuantityStoreBefore(quantityStoreBefore);
                        Long quantityImport = warehouseMap.get(warehouseId).get(productId);
                        //
                        ssi.setQuantity(ssi.getQuantity() + quantityImport);
                        storeStoreInventoryRepository.save(ssi);
                        //
                        storeImportDetail.setQuantityImport(quantityImport);
                        storeImportDetail.setQuantityWarehouseAfter(quantityWarehouseBefore - quantityImport);
//                       storeImportDetail.setQuantityStoreAfter(quantityStoreBefore + quantityImport);
                        storeImportDetail.addWarehouseWarehouseInventory(wwi);
                        storeImportDetail.addStoreStoreInventory(ssi);
                        wwi.setQuantity(quantityWarehouseBefore - quantityImport);
                        //case chưa có product trong storeinventory
                    } else {
                        System.out.println("chay vo day 2");
                        // new product trong storeinventory
                        StoreInventory storeInventory = new StoreInventory();
                        storeInventory.setName(productWarehouse.getName());
                        storeInventory.setProductCode(productWarehouse.getProductCode());
                        storeInventory.setDescription(productWarehouse.getDescription());
                        storeInventory.setBrand(productWarehouse.getSuplier());
                        storeInventory.addCategory(productWarehouse.getCategory());
                        storeInventoryRepository.save(storeInventory);
                        //
                        System.out.println("save product vo store thanh cong");
                        //
                        // new mối quan hệ giữa store và storeinventory
                        Store_StoreInventory ssi = new Store_StoreInventory();
                        ssi.setStore(store);
                        ssi.addStoreInventory(storeInventory);
                        ssi.setQuantity(0L);
                        storeStoreInventoryRepository.save(ssi);
                        //
                        System.out.println("save ssi thanh cong");
                        //
                        //go block
                        if (importSummaryMap.containsKey(productId)) {
                            ImportStoreSummaryBussiness importStoreSummaryBussiness = importSummaryMap.get(productId);
                            importStoreSummaryBussiness.setTotalQuantityImport(importStoreSummaryBussiness.getTotalQuantityImport() + warehouseMap.get(warehouseId).get(productId));
                            importStoreSummaryBussiness.setQuantityStoreAfter(importStoreSummaryBussiness.getQuantityStoreAfter() + warehouseMap.get(warehouseId).get(productId));
                            importSummaryMap.put(productId,importStoreSummaryBussiness);
                        }else {
                            ImportStoreSummaryBussiness importStoreSummaryBussiness = new ImportStoreSummaryBussiness();
                            importStoreSummaryBussiness.setQuantityStoreBefore(ssi.getQuantity());
                            importStoreSummaryBussiness.setTotalQuantityImport(warehouseMap.get(warehouseId).get(productId));
                            importStoreSummaryBussiness.setProductId(productId);
                            importStoreSummaryBussiness.setProductCode(productWarehouse.getProductCode());
                            importStoreSummaryBussiness.setProductName(productWarehouse.getName());
                            importStoreSummaryBussiness.setQuantityStoreAfter(ssi.getQuantity() + warehouseMap.get(warehouseId).get(productId));
                            importStoreSummaryBussiness.getStoreImportDetailList().add(storeImportDetail);
                            importSummaryMap.put(productId,importStoreSummaryBussiness);
                        }
                        //
                        Long quantityWarehouseBefore = wwi.getQuantity();
                        System.out.println("wwi: " + quantityWarehouseBefore);
                        storeImportDetail.setQuantityWarehouseBefore(quantityWarehouseBefore);
//                    Store_StoreInventory ssi = storeStoreInventoryRepository.findStoreStoreInventory(storeId,storeInventory.getId());
//                    if (Objects.isNull(ssi)) System.out.println("null r");
//                    else System.out.println("k null");
//                    System.out.println("Result: " + ssi.toString());
//                       Long quantityStoreBefore = ssi.getQuantity();
//                       System.out.println("Can tim 2: " + quantityStoreBefore);
//                       storeImportDetail.setQuantityStoreBefore(quantityStoreBefore);
                        Long quantityImport = warehouseMap.get(warehouseId).get(productId);
                        ssi.setQuantity(ssi.getQuantity() + quantityImport);
                        storeStoreInventoryRepository.save(ssi);
                        //
                        storeImportDetail.setQuantityImport(quantityImport);
                        storeImportDetail.setQuantityWarehouseAfter(quantityWarehouseBefore - quantityImport);
//                       storeImportDetail.setQuantityStoreAfter(quantityStoreBefore + quantityImport);

                        storeImportDetail.addWarehouseWarehouseInventory(wwi);
                        storeImportDetail.addStoreStoreInventory(ssi);
                        wwi.setQuantity(quantityWarehouseBefore - quantityImport);
                    }
                    //go block


                    warehouseWarehouseInventoryRepository.save(wwi);
                    storeImportDetailRepository.save(storeImportDetail);
                }
            }
        }
        for (UUID productIdOfImportSumMap:importSummaryMap.keySet()) {
            System.out.println("Data can check");
            System.out.println("productId: " + productIdOfImportSumMap + "| " + importSummaryMap.get(productIdOfImportSumMap).toString());
            ImportStoreSummary importStoreSummary = new ImportStoreSummary();
            importStoreSummary.setProductId(importSummaryMap.get(productIdOfImportSumMap).getProductId());
            importStoreSummary.setProductName(importSummaryMap.get(productIdOfImportSumMap).getProductName());
            importStoreSummary.setProductCode(importSummaryMap.get(productIdOfImportSumMap).getProductCode());
            importStoreSummary.setQuantityStoreBefore(importSummaryMap.get(productIdOfImportSumMap).getQuantityStoreBefore());
            importStoreSummary.setTotalQuantityImport(importSummaryMap.get(productIdOfImportSumMap).getTotalQuantityImport());
            importStoreSummary.setQuantityStoreAfter(importSummaryMap.get(productIdOfImportSumMap).getQuantityStoreAfter());
            importStoreSummary.setStoreImportId(storeImport.getId());
            importStoreSummary.addStore(store);
            importStoreSummary.addStoreImportDetails(importSummaryMap.get(productIdOfImportSumMap).getStoreImportDetailList());
            importStoreSummaryRepository.save(importStoreSummary);
            System.out.println("add " + productIdOfImportSumMap + " succesfully");
        }
        System.out.println("sucess nha chúc mừng !! ");

    }

    @Override
    public ImportStoreReportResDto reportImportStore(UUID importStoreId, UUID storeId) {
        return toImportStoreReportResDto(importStoreId, storeId);
    }

    public ImportStoreReportResDto toImportStoreReportResDto(UUID importStoreId, UUID storeId) {
        ImportStoreReportResDto storeDto = null;
        Store storeEntity = storeRepository.findById(storeId).get();
//        StoreImport storeImportEntity = storeImportRepository.findById(importStoreId).get();
        if (storeEntity != null) {
            //start set data cho ImportStoreReportResDto (object gốc)
            storeDto = new ImportStoreReportResDto();
            storeDto.setId(storeEntity.getId());
            storeDto.setName(storeEntity.getName());
            // end set data cho ImportStoreReportResDto (object gốc)
            Optional<StoreImport> storeImportOp = storeEntity.getStoreImportList().stream().filter(storeImport -> storeImport.getId().equals(importStoreId)).findFirst();
            if (storeImportOp.isPresent()) {
                StoreImport storeImportEntity = storeImportOp.get();  // lấy Entity store import
                // new storeImportDto
                ImportStoreReportResDto.StoreImportDto storeImportDto = new ImportStoreReportResDto.StoreImportDto();
                // start set data cho StoreImportDto
                storeImportDto.setId(storeImportEntity.getId());
                //Start set audit cho storeImportDto
                ImportStoreReportResDto.StoreImportDto.AuditDto auditDto = new ImportStoreReportResDto.StoreImportDto.AuditDto();
                auditDto.setCreatedAt(storeImportEntity.getAudit().getCreatedAt());
                auditDto.setUpdatedAt(storeImportEntity.getAudit().getUpdatedAt());
                auditDto.setCreatedBy(storeImportEntity.getAudit().getCreatedBy());
                auditDto.setUpdatedBy(storeImportEntity.getAudit().getUpdatedBy());
                storeImportDto.setAudit(auditDto);
                //End set audit cho storeImportDto

                //new 1 List ImportStoreSummaryDto để gắn vô StoreImportDto (List<ImportStoreSummaryDto> importStoreSummaryDtoList)
                List<ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto> importStoreSummaryDtoList = importStoreSummaryDtoList = new ArrayList<>();
                // lấy Entity importStoreSumary where importStoreSummary.importId = :importStoreId
                List<ImportStoreSummary> importStoreSummaryListEntity = importStoreSummaryRepository.findImportStoreSummarisBy(importStoreId);
                //new StoreImportDetailDto list
                List<ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto> storeImportDetailDtoList = new ArrayList<>();
                //new ImportStoreSummaryDto
                ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto importStoreSummaryDto = null;
                // duyệt list importstoresummary entity lấy từng importStoreSummary ra
                for (ImportStoreSummary importStoreSummary : importStoreSummaryListEntity) {
                    // start set data cho importStoreSummaryDto
                    importStoreSummaryDto = new ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto();
                    importStoreSummaryDto.setId(importStoreSummary.getId());
                    importStoreSummaryDto.setProductName(importStoreSummary.getProductName());
                    importStoreSummaryDto.setProductCode(importStoreSummary.getProductCode());
                    importStoreSummaryDto.setQuantityStoreBefore(importStoreSummary.getQuantityStoreBefore());
                    importStoreSummaryDto.setQuantityStoreAfter(importStoreSummary.getQuantityStoreAfter());
                    // end set data cho importStoreSummaryDto

                    // có importStoreSummaryDto rồi thì add vô list ImportStoreSummaryDto của StoreImportDto
                    importStoreSummaryDtoList.add(importStoreSummaryDto);

                    //new list entity StoreImportDetail
                    List<StoreImportDetail> storeImportDetailListEntity = importStoreSummary.getStoreImportDetailList();
                    //duyệt từng storeImportDetail của list storeImportDetailListEntity
                    for (StoreImportDetail storeImportDetail : storeImportDetailListEntity) {
                        //start set data cho storeImportDetailDto
                        ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto storeImportDetailDto =
                                new ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto();
                        storeImportDetailDto.setId(storeImportDetail.getId());
                        // có storeImportDetail thì add vô list StoreImportDetailDto của ImportStoreSummaryDto
                        storeImportDetailDtoList.add(storeImportDetailDto);
                        // new Warehouse_WarehouseInventoryDto
                        ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto.Warehouse_WarehouseInventoryDto warehouseWarehouseInventoryDto =
                                new ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto.Warehouse_WarehouseInventoryDto();
                        // get Warehouse_WarehouseInventory entity bằng storeImportDetail nối lên Warehouse_WarehouseInventory
                        Warehouse_WarehouseInventory warehouseWarehouseInventoryEntity = storeImportDetail.getWarehouseWarehouseInventory();
                        //start set data cho warehouseWarehouseInventoryDto
                        warehouseWarehouseInventoryDto.setId(warehouseWarehouseInventoryEntity.getId());
                        // start lấy tạm trước data của warehouseWarehouseInventory
                        Long quantityWarehouseBefore = warehouseWarehouseInventoryEntity.getQuantity();
                        Long quantityImport = storeImportDetail.getQuantityImport();
                        Long quantityWarehouseAfter = quantityWarehouseBefore - quantityImport;
                        //end lấy tạm trước data của warehouseWarehouseInventory

                        //set Warehouse_WarehouseInventoryDto cho class StoreImportDetailDto
                        storeImportDetailDto.setWarehouseWarehouseInventoryDto(warehouseWarehouseInventoryDto);
                        // lấy ra warehouse entity bằng Warehouse_WarehouseInventory nối qua Warehouse
                        Warehouse warehouseE = warehouseWarehouseInventoryEntity.getWarehouse();
                        // new WarehouseDto và start set data cho WarehouseDto
                        ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto.Warehouse_WarehouseInventoryDto.WarehouseDto warehouseDto
                                = new ImportStoreReportResDto.StoreImportDto.ImportStoreSummaryDto.StoreImportDetailDto.Warehouse_WarehouseInventoryDto.WarehouseDto();
                        warehouseDto.setId(warehouseE.getId());
                        warehouseDto.setName(warehouseE.getName());
                        warehouseDto.setQuantityWarehouseBefore(quantityWarehouseBefore);
                        warehouseDto.setQuantityImport(quantityImport);
                        warehouseDto.setQuantityWarehouseAfter(quantityWarehouseAfter);
                        //end set data cho WarehouseDto

                        // có warehouseDto rồi thì set warehouseDto cho class Warehouse_WarehouseInventoryDto
                        warehouseWarehouseInventoryDto.setWarehouseDto(warehouseDto);
                    }
                }
                // set storeImportDetailDto list cho class ImportStoreSummaryDto
                importStoreSummaryDto.setStoreImportDetailDtoList(storeImportDetailDtoList); // set List StoreImportDetailDto
                // set importStoreSummaryDto cho class StoreImportDto
                storeImportDto.setImportStoreSummaryDtoList(importStoreSummaryDtoList); // set List importStoreSumary
                //set storeImportDto cho class ImportStoreReportResDto
                storeDto.setStoreImportDto(storeImportDto);

            }
        }
        return storeDto;
    }

    //---------------------------
    public double haversine(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c; // khoảng cách tính bằng km
    }

    private final Supplier<StoreNotFoundException> handlerStoreNotFound = () ->
            new StoreNotFoundException("The Store Isn't Exsist");

    public XSSFWorkbook createExcelFile(ImportStoreReqDto dto) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        int rowIndex;
        int idCellIndex = 3;
        int codeCellIndex = 4;
        int nameCellIndex = 5;
        int quantityInStockIndex = 6;
        int unitCellIndex = 7;
        int quantityCellIndex = 8;
        XSSFSheet sheet = workbook.createSheet("Products");
        sheet.setColumnWidth(codeCellIndex, 9216);
        sheet.setColumnWidth(nameCellIndex, 9216);
        sheet.setColumnWidth(unitCellIndex, 7168);
        sheet.setColumnWidth(quantityCellIndex, 7168);
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);
        CellStyle boldCenterStyle = workbook.createCellStyle();
        boldCenterStyle.setFont(boldFont);
        boldCenterStyle.setAlignment(HorizontalAlignment.CENTER);
        boldCenterStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        CellStyle lockedStyle = workbook.createCellStyle();
        lockedStyle.setLocked(true);
        CellStyle unlockedStyle = workbook.createCellStyle();
        unlockedStyle.setLocked(false);

        //-------------------
        int rowNum = 6;
        int rowHeader = 7;
        for (UUID warehosueId : dto.getWarehouseIdList()) {
            Warehouse warehouse = warehouseRepository.findById(warehosueId).get();
            System.out.println("Warehouse: " + warehouse.getId());
            Row warehouseInfo = sheet.createRow(rowNum);
            Cell idWarehouseCell = warehouseInfo.createCell(2);
            idWarehouseCell.setCellValue(warehouse.getId().toString());
            Cell nameWarehouseCell = warehouseInfo.createCell(3);
            nameWarehouseCell.setCellValue(warehouse.getName());
            Row header = sheet.createRow(rowHeader);
            Cell idHeaderCell = header.createCell(3);
            idHeaderCell.setCellValue("PRODUCT ID");
            idHeaderCell.setCellStyle(boldCenterStyle);
            Cell productCodeHeaderCell = header.createCell(4);
            productCodeHeaderCell.setCellValue("PRODUCT CODE");
            productCodeHeaderCell.setCellStyle(boldCenterStyle);
            Cell productNameHeaderCell = header.createCell(5);
            productNameHeaderCell.setCellValue("PRODUCT NAME");
            productNameHeaderCell.setCellStyle(boldCenterStyle);
            Cell quantityInStockHeaderCell = header.createCell(6);
            quantityInStockHeaderCell.setCellValue("QuantityInStock");
            quantityInStockHeaderCell.setCellStyle(boldCenterStyle);
            Cell productUnitHeaderCell = header.createCell(7);
            productUnitHeaderCell.setCellValue("PRODUCT UNIT");
            productUnitHeaderCell.setCellStyle(boldCenterStyle);
            Cell productQuantityHeaderCell = header.createCell(8);
            productQuantityHeaderCell.setCellValue("PRODUCT QUANTITY");
            productQuantityHeaderCell.setCellStyle(boldCenterStyle);
            rowIndex = rowHeader;
            System.out.println("Warehouse Id: " + warehouse.getId());
            System.out.println("size of product: " + warehouse.getWarehouseWarehouseInventoryList().size());
            for (Warehouse_WarehouseInventory data : warehouse.getWarehouseWarehouseInventoryList()) {
                System.out.println("in ra");
                ++rowIndex;
                Row row = sheet.createRow(rowIndex);
                Cell idCell = row.createCell(idCellIndex);
                idCell.setCellValue(data.getWarehouseInventory().getId().toString());
                idCell.setCellStyle(lockedStyle);
                Cell codeCell = row.createCell(codeCellIndex);
                codeCell.setCellValue(data.getWarehouseInventory().getProductCode());
                codeCell.setCellStyle(lockedStyle);
                Cell nameCell = row.createCell(nameCellIndex);
                nameCell.setCellValue(data.getWarehouseInventory().getName());
                nameCell.setCellStyle(lockedStyle);
                Cell quantityInStock = row.createCell(quantityInStockIndex);
                quantityInStock.setCellValue(data.getQuantity());
                quantityInStock.setCellStyle(lockedStyle);
                Cell unitCell = row.createCell(unitCellIndex);
                unitCell.setCellValue(data.getWarehouseInventory().getUnitOfMeasure());
                unitCell.setCellStyle(lockedStyle);

                //----
                Cell quantityCell = row.createCell(quantityCellIndex);
                System.out.println("dto list: " + dto.getProductImportStoreDtoList().toString());
                System.out.println("product id: " + data.getWarehouseInventory().getId());
                System.out.println("check true/false: " + dto.getProductImportStoreDtoList().stream().anyMatch(p -> p.getProductId().equals(data.getWarehouseInventory().getId())));
                if (dto.getProductImportStoreDtoList().stream().anyMatch(p -> p.getProductId().equals(data.getWarehouseInventory().getId()))) {
                    System.out.println("hello :" + data.getWarehouseInventory().getId());
                    int indexOfProduct = dto.getProductImportStoreDtoList().indexOf(indexOfProductList(data.getWarehouseInventory().getId(), dto.getProductImportStoreDtoList()));
                    System.out.println("index: " + indexOfProduct);
                    quantityCell.setCellValue(dto.getProductImportStoreDtoList().get(indexOfProduct).getQuantity());
                    quantityCell.setCellStyle(unlockedStyle);
                } else {
                    quantityCell.setCellValue(0.0);
                    quantityCell.setCellStyle(unlockedStyle);
                }
            }
            rowNum = rowIndex + 3;
            rowHeader = rowNum + 1;
        }


        sheet.setColumnHidden(idCellIndex, true);
//        sheet.protectSheet("123456");
//        sheet.lockFormatColumns(false);
//        sheet.lockFormatRows(true);
        System.out.println("✅ File Excel được tạo: product-test.xlsx");
        return workbook;
    }

    public ImportStoreReqDto.ProductImportStoreDto indexOfProductList(UUID target, List<ImportStoreReqDto.ProductImportStoreDto> list) {
        for (ImportStoreReqDto.ProductImportStoreDto data : list) {
            if (data.getProductId().equals(target)) {
                return data;
            }
        }
        return null;
    }

    @SneakyThrows
    public Map<UUID, Map<UUID, Long>> readImportStoreExcelFile(MultipartFile file) {

//        String filePath = "D:\\Code\\RetailManagerv2\\mydata\\import-store.xlsx"; // đường dẫn file Excel
//        FileInputStream fis = new FileInputStream(filePath);
        InputStream is = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        boolean warehouseFlag = false;
        boolean productFlag = false;
        Map<UUID, Map<UUID, Long>> warehouseMap = new HashMap<>();
        Map<UUID, Long> productMap = null;
        UUID warehouseId = null;
        UUID productId = null;
        Long quantity;


        for (Row row : sheet) {
            for (Cell c : row) {
                if (c.getCellType().equals(CellType.STRING)) {
                    if (c.getStringCellValue().equals("warehouseflag")) {
                        warehouseFlag = true;
                        continue;
                    }
                    if (c.getStringCellValue().equals("productflagopen")) {
                        productFlag = true;
                        productMap = new HashMap<>();
                        continue;
                    }
                    if (c.getStringCellValue().equals("productflagclose")) {
                        productFlag = false;
                        continue;
                    }
                }
                if (warehouseFlag)
                    if (c.getCellType().equals(CellType.STRING)) {
                        System.out.println("warehouse id: " + c.getStringCellValue());
                        warehouseId = UUID.fromString(c.getStringCellValue());
                        warehouseFlag = false;
                    }
                if (productFlag) {

                    if (c.getColumnIndex() == 3 && c.getCellType().equals(CellType.STRING)) {
                        System.out.println("product id: " + c.getStringCellValue());
                        productId = UUID.fromString(c.getStringCellValue());
                    }
                    if (c.getColumnIndex() == 8 && c.getCellType().equals(CellType.NUMERIC)) {
                        System.out.println("product id: " + c.getNumericCellValue());
                        quantity = Long.valueOf((long) c.getNumericCellValue());
                        productMap.put(productId, quantity);
                        warehouseMap.put(warehouseId, productMap);
                    }
                }
            }
        }

        for (UUID wId : warehouseMap.keySet()) {
            System.out.println("warehouse id: " + wId);
            for (UUID pId : warehouseMap.get(wId).keySet()) {
                System.out.println("product id: " + pId + " " + "quantity : " + warehouseMap.get(wId).get(pId));
            }
        }

        workbook.close();
        is.close();
        return warehouseMap;
    }
}
