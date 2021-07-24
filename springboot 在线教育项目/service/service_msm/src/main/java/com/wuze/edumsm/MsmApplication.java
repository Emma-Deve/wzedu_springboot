package com.wuze.edumsm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-28 17:44:48
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan({"com.wuze"})
public class MsmApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsmApplication.class,args);
    }
}
