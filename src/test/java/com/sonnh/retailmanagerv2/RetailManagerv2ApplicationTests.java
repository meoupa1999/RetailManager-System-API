package com.sonnh.retailmanagerv2;

import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.repository.AccountRepository;
import com.sonnh.retailmanagerv2.data.repository.StoreRepository;
import com.sonnh.retailmanagerv2.data.repository.WarehouseInventoryRepository;
import com.sonnh.retailmanagerv2.data.repository.WarehouseRepository;
import com.sonnh.retailmanagerv2.dto.request.admin.SuggestWarehouseReqDto;
import com.sonnh.retailmanagerv2.dto.response.SuggestWarehouseResDto;
import com.sonnh.retailmanagerv2.service.interfaces.StoreImportService;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest
@Commit // hoặc bỏ hẳn @Transactional
class RetailManagerv2ApplicationTests {
    private static final double EARTH_RADIUS_KM = 6371.0; // Bán kính Trái Đất (km)
    @Autowired
    WarehouseInventoryRepository warehouseInventoryRepository;
    @Autowired
    WarehouseRepository warehouseRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreImportService storeImportService;
    @Autowired
    private AccountRepository accountRepository;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void myTest() {
        Map<UUID, Map<UUID, Long>> map = new HashMap<>();
        List<Warehouse> warehouseList = warehouseRepository.findAll();
        for (Warehouse value1 : warehouseList) {
            Map<UUID, Long> productMap = new HashMap<>();
            for (Warehouse_WarehouseInventory value2 : value1.getWarehouseWarehouseInventoryList()) {
                productMap.put(value2.getWarehouseInventory().getId(), value2.getQuantity());
            }
            map.put(value1.getId(), productMap);
        }
        for (UUID key1 : map.keySet()) {
            System.out.println("key ngoài : " + key1);
            for (UUID key2 : map.get(key1).keySet()) {
                System.out.println(" key trong: " + key2 + " value trong: " + map.get(key1).get(key2));
            }
        }

//----------------
        SuggestWarehouseReqDto dto1 = new SuggestWarehouseReqDto(UUID
                .fromString("d5826dcc-4e83-4fb1-abb2-efca488005a7"), 400L);
        SuggestWarehouseReqDto dto2 = new SuggestWarehouseReqDto(UUID
                .fromString("e0d776e0-6fee-42a8-bca1-ddf32e0ede17"), 100L);
        SuggestWarehouseReqDto dto3 = new SuggestWarehouseReqDto(UUID
                .fromString("0626b644-a110-4b6d-8f13-08d0070b9e8e"), 200L);
        SuggestWarehouseReqDto dto4 = new SuggestWarehouseReqDto(UUID
                .fromString("3b901cbb-3eab-4b7f-ba2b-28faaf055a25"), 30L);
        SuggestWarehouseReqDto dto5 = new SuggestWarehouseReqDto(UUID
                .fromString("f000c79d-ba41-48ab-83d8-00ec2ff27131"), 40L);
//---------------------
        List<SuggestWarehouseReqDto> list = new ArrayList<>();
        list.add(dto1);
        list.add(dto2);
        list.add(dto3);
        list.add(dto4);
        list.add(dto5);
        // ------------------------
        Long total = 0L;

        // lấy tổng quantity store cần
        for (SuggestWarehouseReqDto value : list) {
            total += value.getQuantity();
        }
        System.out.println("total: " + total);
        List<SuggestWarehouseResDto> suggestWarehouseResDtoList = new ArrayList<>();
        Long result = 0L;
        Double supplyScore = 0.0;
        Double weight = 0.0;
        Double supplyRatio = 0.0;
        Double distance = 0.0;
        Double distanceScore = 0.0;
        Double warehouseLat = 0.0;
        Double warehouseLon = 0.0;
        Double storeLat = 0.0;
        Double storeLon = 0.0;
        Double totalScore = 0.0;

        Store store = storeRepository.findById(UUID.fromString("af516852-1914-4d69-8729-189ef6e80238")).get();

        for (UUID key : map.keySet()) {
            UUID warehouseId = key;
            System.out.println("WarehouseId: " + key);
            for (SuggestWarehouseReqDto value : list) {

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
        System.out.println("---------------------------");
        for (SuggestWarehouseResDto value : suggestWarehouseResDtoList) {
            System.out.println(value.toString());
        }
    }

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

    @Test
    @SneakyThrows
    void myTest2() {
        String filePath = "D:\\Code\\RetailManagerv2\\mydata\\import-store.xlsx"; // đường dẫn file Excel
        FileInputStream fis = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        boolean warehouseFlag = false;
        boolean productFlag = false;
        Map<UUID, Map<UUID, Long>> warehouseMap = new HashMap<>();
        Map<UUID, Long> productMap = new HashMap<>();
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
        fis.close();
    }

    @Test
    @Transactional
    void myTest3() {
//        storeImportService.importProductForStore(UUID.fromString("af516852-1914-4d69-8729-189ef6e80238"));
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String plainPassword = "123456";

        Account account =  accountRepository.findById(UUID.fromString("acf30ca3-3da1-474e-a41a-146fabe05e3d")).get();
        account.setPassword(passwordEncoder.encode(plainPassword));
    }


}
