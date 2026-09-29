package com.sup.supplierbiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.sup.supplierbiz.mapper")
public class SupplierBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierBizApplication.class, args);
    }

}
