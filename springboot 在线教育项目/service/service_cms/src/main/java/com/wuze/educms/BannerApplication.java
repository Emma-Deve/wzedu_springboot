package com.wuze.educms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-27 10:19:09
 */
@SpringBootApplication
@ComponentScan({"com.wuze"})
@MapperScan("com.wuze.educms.mapper")   //扫描 mapper 文件夹的文件
@EnableDiscoveryClient  //nacos注册（gateway才能发现）
public class BannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(BannerApplication.class,args);
    }
}
