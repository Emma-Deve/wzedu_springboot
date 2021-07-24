package com.wuze.edustatistics;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-06 17:43:50
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.wuze"})
@EnableDiscoveryClient  //nacos注册
@EnableFeignClients //feign远程调用
@MapperScan("com.wuze.edustatistics.mapper")
@EnableScheduling   //定时任务
public class StaApplication {
    public static void main(String[] args) {
        SpringApplication.run(StaApplication.class,args);
    }
}
