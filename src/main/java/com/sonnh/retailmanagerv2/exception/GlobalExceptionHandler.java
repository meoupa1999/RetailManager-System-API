package com.sonnh.retailmanagerv2.exception;

import com.sonnh.retailmanagerv2.exception.store_exception.StoreNotFoundException;
import com.sonnh.retailmanagerv2.exception.warehouse_exception.WarehouseNotFoundException;
import com.sonnh.retailmanagerv2.exception.warehouseinventory_exception.WarehouseInventoryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public GlobalExceptionHandler() {
    }

    @ExceptionHandler({StoreNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleStoreNotFound(StoreNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        log.error(ex.getMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({WarehouseNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleWarehouseNotFound(WarehouseNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .timestamp(Instant.now())
                .build();
        log.error(ex.getMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({WarehouseInventoryNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleWarehouseInventoryNotFound(WarehouseInventoryNotFoundException ex, HttpServletRequest request) {
        ErrorResponse error = ErrorResponse.builder().status(HttpStatus.NOT_FOUND.value()).message(ex.getMessage()).path(request.getRequestURI()).timestamp(Instant.now()).build();
        log.error(ex.getMessage());
        return new ResponseEntity(error, HttpStatus.NOT_FOUND);
    }

}
