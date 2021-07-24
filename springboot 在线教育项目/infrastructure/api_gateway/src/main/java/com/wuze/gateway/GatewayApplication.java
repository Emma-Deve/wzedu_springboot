package com.wuze.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-08 03:58:21
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wuze"})
@EnableDiscoveryClient  //nacos 注册
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }
}
