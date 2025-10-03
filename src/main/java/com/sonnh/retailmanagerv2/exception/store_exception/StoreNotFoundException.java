package com.sonnh.retailmanagerv2.exception.store_exception;

public class StoreNotFoundException extends RuntimeException{
    public StoreNotFoundException(String message) {
        super(message);
    }
}
