package com.sonnh.retailmanagerv2.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ErrorResponse {
    private int status;
    private String message;
    String path;
    private Instant timestamp;
}
