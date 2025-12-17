package com.sonnh.retailmanagerv2.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Getter
@Configuration
public class StorageProperties{
    @Value("${azure.storage.connection-string}")
    private String connectionString;
    @Value("${azure.storage.container-name}")
    private String containerName;
}
