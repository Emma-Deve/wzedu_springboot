package com.wuze.eduservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-03 16:52:06
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wuze"})
@EnableDiscoveryClient  //nacos 注册
@EnableFeignClients //feign 服务调用
public class EduApplication {
    public static void main(String[] args) {
        SpringApplication.run(EduApplication.class,args);
    }
}
