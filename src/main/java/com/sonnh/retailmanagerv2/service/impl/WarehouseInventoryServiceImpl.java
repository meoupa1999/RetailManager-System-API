package com.sonnh.retailmanagerv2.service.impl;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.sonnh.retailmanagerv2.config.StorageProperties;
import com.sonnh.retailmanagerv2.data.domain.*;
import com.sonnh.retailmanagerv2.data.repository.*;
import com.sonnh.retailmanagerv2.data.specification.WarehouseInventorySpecification;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryCreateReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.WarehouseInventoryUpdateReqDto;
import com.sonnh.retailmanagerv2.dto.response.PageImplResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryDetailResDtov2;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryExcelResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.WarehouseInventoryResDto;
import com.sonnh.retailmanagerv2.exception.warehouse_exception.WarehouseNotFoundException;
import com.sonnh.retailmanagerv2.exception.warehouseinventory_exception.WarehouseInventoryNotFoundException;
import com.sonnh.retailmanagerv2.mapper.ProductMapper;
import com.sonnh.retailmanagerv2.service.interfaces.WarehouseInventoryService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class WarehouseInventoryServiceImpl implements WarehouseInventoryService {
    private final ConfigRepository configRepository;
    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final Warehouse_WarehouseInventoryRepository warehouseWarehouseInventoryRepository;
    private final WarehouseImportDetailRepository warehouseImportDetailRepository;
    private final WarehouseImportRepository warehouseImportRepository;
    private final ProductMapper productMapper;
    private final StorageProperties storageProperties;
    private final ProductImageRepository productImageRepository;

    public PageImplResDto getAllWarehouseInventory(String name, String suplier, Double purchasePriceGreaterThan, Double purchasePriceLessThan, Double salePriceGreaterThan, Double salePriceLessThan, Integer page, Integer size) {
        Specification<WarehouseInventory> spec = Specification.where((null));
        if (StringUtils.hasText(name)) {
            spec = spec.and(WarehouseInventorySpecification.nameContains(name));
        }

        if (StringUtils.hasText(suplier)) {
            spec = spec.and(WarehouseInventorySpecification.suplierContains(suplier));
        }

        if (purchasePriceGreaterThan != null) {
            spec = spec.and(WarehouseInventorySpecification.purchasePriceGreaterThan(purchasePriceGreaterThan));
        }

        if (purchasePriceLessThan != null) {
            spec = spec.and(WarehouseInventorySpecification.purchasePriceLessThan(purchasePriceLessThan));
        }

        if (salePriceGreaterThan != null) {
            spec = spec.and(WarehouseInventorySpecification.salePriceGreaterThan(salePriceGreaterThan));
        }

        if (salePriceLessThan != null) {
            spec = spec.and(WarehouseInventorySpecification.salePriceLessThan(salePriceLessThan));
        }

        spec = spec.and(WarehouseInventorySpecification.isActive());
        PageRequest pageable = PageRequest.of(page != null && page > 0 ? page - 1 : 0, size != null && size > 0 ? size : 100, Sort.by(Sort.Direction.DESC, new String[]{"audit.updatedAt"}));
        Page<WarehouseInventory> productPage = warehouseInventoryRepository.findAll(spec, pageable);
//        ProductMapper var10001 = this.productMapper;
//        Objects.requireNonNull(var10001);
        Page<WarehouseInventoryResDto> dto = productPage.map(productMapper::toWarehouseInventoryResDto);
        return PageImplResDto.fromPage(dto);
    }

    public WarehouseInventoryDetailResDtov2 findWarehouseProductById(UUID id) {
        WarehouseInventory entity = this.warehouseInventoryRepository
                .findById(id)
                .filter(product -> product.getAudit().getIsActive())
                .orElseThrow(handlerWarehouseInventoryNotFound);
        return Optional.ofNullable(entity).map(productMapper::toWarehouseInventoryDetailDtov2).get();
    }

    @Transactional
    @SneakyThrows
    public UUID createWarehouseProduct(MultipartFile[] files,WarehouseInventoryCreateReqDto dto) {
        WarehouseInventory entity = Optional.ofNullable(dto).map(productMapper::toWarehouseInventoryCreateEntity).get();
//        ProductMapper var10001 = this.productMapper;
//        Objects.requireNonNull(var10001);
//        WarehouseInventory entity = (WarehouseInventory)var10000.map(var10001::toWarehouseInventoryCreateEntity).get();
        Config configEntity = configRepository.findById(1L).get();
        String productCode = this.productCode(configEntity.getCodeNum());
        entity.setProductCode(productCode);
        configEntity.setCodeNum(configEntity.getCodeNum() + 1);
        String url= null;
        List<ProductImage> productImageList = new ArrayList<>();
        for (MultipartFile file:files) {
          url =  uploadFile(file.getOriginalFilename(),file.getInputStream(),file.getSize(),file.getContentType());
          ProductImage productImage = new ProductImage();
          productImage.setUrl(url);
          productImageRepository.save(productImage);
          productImageList.add(productImage);
        }
        entity.addImages(productImageList);
        return warehouseInventoryRepository.save(entity).getId();
    }

    @Transactional
    public UUID updateWarehouseProduct(UUID id, WarehouseInventoryUpdateReqDto dto) {
        WarehouseInventory entity = warehouseInventoryRepository.findById(id)
                .filter(product -> product.getAudit().getIsActive())
                .orElseThrow(this.handlerWarehouseInventoryNotFound);
        if (!Objects.isNull(dto.getName())) {
            entity.setName(dto.getName());
        }

        if (!Objects.isNull(dto.getSuplier())) {
            entity.setSuplier(dto.getSuplier());
        }

        if (!Objects.isNull(dto.getPurchasePrice())) {
            entity.setPurchasePrice(dto.getPurchasePrice());
        }

        if (!Objects.isNull(dto.getSalePrice())) {
            entity.setSalePrice(dto.getSalePrice());
        }

        if (!Objects.isNull(dto.getWarrantyPeriod())) {
            entity.setWarrantyPeriod(dto.getWarrantyPeriod());
        }

        return warehouseInventoryRepository.save(entity).getId();
    }

    public XSSFWorkbook exportProductToExcel(UUID warehouseId) {
        List<WarehouseInventory> warehouseInventoryList = warehouseInventoryRepository.findAll().stream()
                       .filter(p -> p.getAudit().getIsActive()).collect(Collectors.toList());
        List<WarehouseInventoryExcelResDto> dto = warehouseInventoryList.stream().map(productMapper::toWarehouseInventoryExcelResDto).collect(Collectors.toList());
        return this.createExcelFile(dto);
    }

    @Transactional
    public void importProductForWarehouse(UUID warehouseId, MultipartFile file) {
        Map<UUID, Long> productInFileMap = this.readExcelFile(file);
        Warehouse warehouseEntity = warehouseRepository.findById(warehouseId).orElseThrow(this.handlerWarehouseNotFound);
        Set<UUID> idProductWarehouseSet = warehouseEntity.getWarehouseWarehouseInventoryList().stream()
                .map(wwi -> wwi.getWarehouseInventory().getId())
                .collect(Collectors.toSet());
        WarehouseImport warehouseImport = new WarehouseImport();
        warehouseImport.addWarehouse(warehouseEntity);
        this.warehouseImportRepository.save(warehouseImport);


        for(UUID key :productInFileMap.keySet()){
//            UUID key = (UUID) var7.next();
            Warehouse_WarehouseInventory wwiEntity;
            Long quantityBefore;
            Long quantityImport;
            Long quantityAfter;
            if (idProductWarehouseSet.contains(key)) {
                wwiEntity = this.warehouseWarehouseInventoryRepository.findWarehouse_WarehouseInventories(warehouseId, key);
                quantityBefore = wwiEntity.getQuantity();
                quantityImport =  productInFileMap.get(key);
                quantityAfter = quantityBefore + quantityImport;
                wwiEntity.setQuantity(wwiEntity.getQuantity() +  productInFileMap.get(key));
                WarehouseImportDetail warehouseImportDetail = new WarehouseImportDetail();
                warehouseImportDetail.addWarehouseImport(warehouseImport);
                warehouseImportDetail.setQuantityBefore(quantityBefore);
                warehouseImportDetail.setQuantityImport(quantityImport);
                warehouseImportDetail.setQuantityAfter(quantityAfter);
                warehouseImportDetail.addWarehouseWarehouseInventory(wwiEntity);
                this.warehouseImportDetailRepository.save(warehouseImportDetail);
            } else {
                wwiEntity = new Warehouse_WarehouseInventory();
                wwiEntity.addWarehouse(warehouseEntity);
                WarehouseInventory product = warehouseInventoryRepository.findById(key).orElseThrow(this.handlerProductNotFound);
                wwiEntity.addProduct(product);
                wwiEntity.setQuantity(productInFileMap.get(key));
                this.warehouseWarehouseInventoryRepository.save(wwiEntity);
                quantityBefore = 0L;
                quantityImport =  productInFileMap.get(key);
                quantityAfter = quantityBefore + quantityImport;
                WarehouseImportDetail warehouseImportDetail = new WarehouseImportDetail();
                warehouseImportDetail.addWarehouseImport(warehouseImport);
                warehouseImportDetail.setQuantityBefore(quantityBefore);
                warehouseImportDetail.setQuantityImport(quantityImport);
                warehouseImportDetail.setQuantityAfter(quantityAfter);
                warehouseImportDetail.addWarehouseWarehouseInventory(wwiEntity);
                this.warehouseImportDetailRepository.save(warehouseImportDetail);
            }
        }
        System.out.println("Thành Công rồi");
    }

    public String productCode(Integer index) {
        String currentYear = String.valueOf(LocalDate.now().getYear());
        return String.format("PRO%s-%05d", currentYear, index);
    }

    public XSSFWorkbook createExcelFile(List<WarehouseInventoryExcelResDto> dto) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        int rowIndex = 7;
        int idCellIndex = 3;
        int codeCellIndex = 4;
        int nameCellIndex = 5;
        int unitCellIndex = 6;
        int quantityCellIndex = 7;
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
        Row header = sheet.createRow(7);
        Cell idHeaderCell = header.createCell(3);
        idHeaderCell.setCellValue("PRODUCT ID");
        idHeaderCell.setCellStyle(boldCenterStyle);
        Cell productCodeHeaderCell = header.createCell(4);
        productCodeHeaderCell.setCellValue("PRODUCT CODE");
        productCodeHeaderCell.setCellStyle(boldCenterStyle);
        Cell productNameHeaderCell = header.createCell(5);
        productNameHeaderCell.setCellValue("PRODUCT NAME");
        productNameHeaderCell.setCellStyle(boldCenterStyle);
        Cell productUnitHeaderCell = header.createCell(6);
        productUnitHeaderCell.setCellValue("PRODUCT UNIT");
        productUnitHeaderCell.setCellStyle(boldCenterStyle);
        Cell productQuantityHeaderCell = header.createCell(7);
        productQuantityHeaderCell.setCellValue("PRODUCT QUANTITY");
        productQuantityHeaderCell.setCellStyle(boldCenterStyle);


        for (WarehouseInventoryExcelResDto data : dto) {
            ++rowIndex;
            Row row = sheet.createRow(rowIndex);
            Cell idCell = row.createCell(idCellIndex);
            idCell.setCellValue(data.getId().toString());
            idCell.setCellStyle(lockedStyle);
            Cell codeCell = row.createCell(codeCellIndex);
            codeCell.setCellValue(data.getProductCode());
            codeCell.setCellStyle(lockedStyle);
            Cell nameCell = row.createCell(nameCellIndex);
            nameCell.setCellValue(data.getName());
            nameCell.setCellStyle(lockedStyle);
            Cell unitCell = row.createCell(unitCellIndex);
            unitCell.setCellValue(data.getUnitOfMeasure());
            unitCell.setCellStyle(lockedStyle);
            Cell quantityCell = row.createCell(quantityCellIndex);
//            if (data.getWarehouse_warehouseInventoryList().size() == 0) {
                quantityCell.setCellValue(0.0);
                quantityCell.setCellStyle(unlockedStyle);
//            }
//            else {
//                quantityCell.setCellValue((data.getWarehouse_warehouseInventoryList().get(0)).getQuantity());
//                quantityCell.setCellStyle(unlockedStyle);
//            }
        }

        sheet.setColumnHidden(idCellIndex, true);
        sheet.protectSheet("123456");
        sheet.lockFormatColumns(false);
        sheet.lockFormatRows(true);
        System.out.println("✅ File Excel được tạo: product-test.xlsx");
        return workbook;
    }
    public Map<UUID, Long> readExcelFile(MultipartFile file) {
        Map<UUID, Long> map = new HashMap();

        try {
            InputStream is = file.getInputStream();

            try {
                XSSFWorkbook workbook = new XSSFWorkbook(is);
                try {
                    XSSFSheet sheet = workbook.getSheet("Products");
                    boolean headerSkip = true;
//                    Iterator var7 = sheet.iterator();

                    for(Row row : sheet) {
//                        Row row = (Row) var7.next();
                        if (headerSkip) {
                            headerSkip = false;
                        } else {
                            Cell quantityCell = row.getCell(7);
                            Double quantityDouble = (Double) this.getCellValue(quantityCell);
                            Long quantity = quantityDouble.longValue();
                            if (quantity > 0L) {
                                Cell idCell = row.getCell(3);
                                String idString = (String) this.getCellValue(idCell);
                                UUID id = UUID.fromString(idString);
                                map.put(id, quantity);
                            }
                        }
                    }
                } catch (Throwable ex1) {
                    try {
                        workbook.close();
                    } catch (Throwable ex2) {
                        ex1.addSuppressed(ex2);
                    }
                    throw ex1;
                }
                workbook.close();
            } catch (Throwable ex3) {
                if (is != null) {
                    try {
                        is.close();
                    } catch (Throwable ex4) {
                        ex3.addSuppressed(ex4);
                    }
                }
                throw ex3;
            }
            if (is != null) {
                is.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    private Object getCellValue(Cell cell) {
        Object result;
        switch (cell.getCellType()) {
            case STRING:
                result = cell.getStringCellValue();
                break;
            case NUMERIC:
                result = cell.getNumericCellValue();
                break;
            case BOOLEAN:
                result = cell.getBooleanCellValue();
                break;
            case FORMULA:
                result = cell.getCellFormula();
                break;
            case BLANK:
                result = "";
                break;
            default:
                result = "UNKNOWN";
        }

        return result;
    }


    private final Supplier<WarehouseInventoryNotFoundException> handlerWarehouseInventoryNotFound = () ->
         new WarehouseInventoryNotFoundException("The Product Of Warehouse Isn't Exsist");

    private final Supplier<WarehouseNotFoundException> handlerWarehouseNotFound = () ->
         new WarehouseNotFoundException("Warehouse Isn't Exsist");

    private final Supplier<WarehouseInventoryNotFoundException> handlerProductNotFound = () ->
         new WarehouseInventoryNotFoundException("Product Isn't Exsist");

    public String uploadFile(String fileName, InputStream data, long size, String contentType) {
        // Tạo client kết nối tới Azure Storage
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .connectionString(storageProperties.getConnectionString())
                .buildClient();

        // Lấy container đã cấu hình
        BlobContainerClient containerClient = blobServiceClient
                .getBlobContainerClient(storageProperties.getContainerName());

        // Lấy blob (tương ứng file cần upload)
        BlobClient blobClient = containerClient.getBlobClient(fileName);

        // Set metadata cho file
        BlobHttpHeaders headers = new BlobHttpHeaders()
                .setContentType(contentType);

        // Upload file (overwrite = true nếu trùng tên thì ghi đè)
        blobClient.upload(data, size, true);
        blobClient.setHttpHeaders(headers);

        // Trả về URL để lưu trong DB
        return blobClient.getBlobUrl();
    }
}
