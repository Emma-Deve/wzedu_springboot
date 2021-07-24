package com.wuze.eduservice.client;

import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-06 02:01:03
 */
//@FeignClient 注解只需要写一个参数name值，不需要写 fallback（使用hystrix 熔断器才需要）
@Component
@FeignClient("service-order")
public interface OrderClientInterface {

    //远程调用 order 模块 “课程是否已经购买” 方法
    //注意1：全路径
    //注意2：@PathVariable("xxx") 注解内写参数
    //根据 课程id 和 用户id 查询订单表中的订单状态
    @ApiOperation(value = "根据 课程id 和 用户id 查询订单表中的订单状态")
    @GetMapping("/eduorder/order/OrderIsBuy/{courseId}/{memberId}")
    public boolean OrderIsBuy(@PathVariable("courseId") String courseId, @PathVariable("memberId") String memberId);
}
