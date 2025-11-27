package com.sonnh.retailmanagerv2;

import com.sonnh.retailmanagerv2.data.domain.Warehouse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class RetailManagerv2Application {

    public static void main(String[] args) {
        SpringApplication.run(RetailManagerv2Application.class, args);
//        Warehouse warehouse = new Warehouse();
//        warehouse.setName("son");
//        System.out.println(warehouse.getName());
    }

}
