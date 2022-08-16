package com.andyfys.yupicenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.andyfys.yupicenter.mapper"})
public class YupiCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(YupiCenterApplication.class, args);
    }

}
