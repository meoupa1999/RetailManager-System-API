package com.sonnh.retailmanagerv2.exception.warehouse_exception;

public class WarehouseNotFoundException extends RuntimeException{
    public WarehouseNotFoundException(String message) {
        super(message);
    }
}
