package com.sup.supplierbiz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 供应商业务启动类
 *
 * @author sup
 * @date 2026-10-09
 */
@SpringBootApplication
@MapperScan("com.sup.supplierbiz.mapper")
public class SupplierBizApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupplierBizApplication.class, args);
    }

}
