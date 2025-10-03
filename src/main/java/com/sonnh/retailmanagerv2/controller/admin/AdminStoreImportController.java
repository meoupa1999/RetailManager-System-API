package com.sonnh.retailmanagerv2.controller.admin;

import com.sonnh.retailmanagerv2.dto.request.admin.ImportStoreReqDto;
import com.sonnh.retailmanagerv2.dto.request.admin.SuggestWarehouseReqDto;
import com.sonnh.retailmanagerv2.dto.response.SuggestWarehouseResDto;
import com.sonnh.retailmanagerv2.dto.response.admin.ImportStoreReportResDto;
import com.sonnh.retailmanagerv2.service.interfaces.StoreImportService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Store Import (Admin)")
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminStoreImportController {
    private final StoreImportService storeImportService;


    @PostMapping("/suggestwarehouse")
    public ResponseEntity<List<SuggestWarehouseResDto>> processDtos(@RequestBody List<SuggestWarehouseReqDto> dto, @RequestParam UUID storeId) {
//        if (dtos == null || dtos.isEmpty()) {
//            return ResponseEntity.badRequest().body(null); // Trả về 400 nếu danh sách rỗng hoặc null
//        }
        List<SuggestWarehouseResDto> result = storeImportService.suggestWarehouse(dto,storeId);
        // Trả về kết quả dưới dạng ResponseEntity với mã trạng thái 200 OK
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @PostMapping({"/exportProduct"})
    public ResponseEntity<byte[]> exportExcel(@RequestBody ImportStoreReqDto dto) {
        try {
            XSSFWorkbook workbook = this.storeImportService.exportExcelImportStore(dto);
            ResponseEntity result;
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                try {
                    workbook.write(out);
                    byte[] bytes = out.toByteArray();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
                    headers.setContentDisposition(ContentDisposition.attachment().filename("import-store.xlsx").build());
                    result = new ResponseEntity(bytes, headers, HttpStatus.OK);
                } catch (Throwable ex1) {
                    try {
                        out.close();
                    } catch (Throwable ex2) {
                        ex1.addSuppressed(ex2);
                    }
                    throw ex1;
                }

                out.close();
            } catch (Throwable ex3) {
                if (workbook != null) {
                    try {
                        workbook.close();
                    } catch (Throwable ex4) {
                        ex3.addSuppressed(ex4);
                    }
                }
                throw ex3;
            }
            if (workbook != null) {
                workbook.close();
            }
            return result;
        } catch (IOException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(
            value = {"/importProductForStore"},consumes = {"multipart/form-data"})
    public ResponseEntity<String> importProductForStore(@RequestParam("storeId") UUID storeId, @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        } else {
            String fileName = file.getOriginalFilename();
            if (fileName != null && (fileName.endsWith(".xlsx") || fileName.endsWith(".xls"))) {
                try {
                    this.storeImportService.importProductForStore(storeId, file);
                    return ResponseEntity.ok("Import thành công");
                } catch (Exception var5) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Import thất bại: " + var5.getMessage());
                }
            } else {
                return ResponseEntity.badRequest().body("Invalid file type, must be Excel");
            }
        }
    }

    @GetMapping("/import-store-report")
    public ResponseEntity<ImportStoreReportResDto> getImportStoreReport(
            @RequestParam("importStoreId") UUID importStoreId,
            @RequestParam("storeId") UUID storeId) {
       ImportStoreReportResDto result = storeImportService.reportImportStore(importStoreId,storeId);
        // Trả về ResponseEntity với mã trạng thái OK (200)
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
