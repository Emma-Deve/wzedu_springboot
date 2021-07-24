package com.wuze.educenter;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-29 00:50:12
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wuze"})
@MapperScan("com.wuze.educenter.mapper")
@EnableDiscoveryClient  //nacos 注册
public class UserCenterApplictaion {
    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplictaion.class,args);
    }
}
