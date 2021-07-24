package com.wuze.eduorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-03 16:15:01
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wuze"})
@MapperScan("com.wuze.eduorder.mapper")
@EnableDiscoveryClient  //nacos 注册
@EnableFeignClients //feign 远程调用
public class OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
