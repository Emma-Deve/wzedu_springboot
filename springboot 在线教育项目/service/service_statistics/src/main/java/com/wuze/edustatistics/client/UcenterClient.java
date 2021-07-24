package com.wuze.edustatistics.client;

import com.wuze.commonutils.R;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-06 18:06:42
 */
@Component
@FeignClient("service-ucenter")
public interface UcenterClient {

    //统计当天有多少用户注册
    //注1：全路径
    //注2：@PathVariable("xxx") 写参数名称
    @ApiModelProperty(value = "统计当天有多少用户注册")
    @PostMapping("/educenter/ucenter-member/getRegisterCount/{day}")
    public R getRegisterCount(@PathVariable("day") String day);
}
