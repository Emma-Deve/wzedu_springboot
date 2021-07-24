package com.wuze.eduorder.client;

import com.wuze.commonutils.OrderCommonClassVo.UcenterMemberOrder;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-03 16:48:16
 */
//注意（说明）：@Feign()的参数2的  fallback属性 是指定 Hystrix 熔断器 触发之后要执行的实现类，与远程调用实现类无关！
//这里应该是不用写参数2的
@Component
//@FeignClient(name = "service-ucenter",fallback = OrderServiceImpl.class)  //参数1：调用接口的模块名称； 参数2：。。。
@FeignClient("service-ucenter") //修正后（2021/02/05）
public interface IMemberOrderClient {

    //根据用户id获取用户信息
    //返回公共类 UcenterMemberOrder （不要返回R，方便待会在其他后端接口取值）
    //注1：@PathVariable("xxx") 里面要写参数名称
    //注2：路径要写全路径
    @ApiModelProperty(value = "根据用户id获取用户信息")
    @PostMapping("/educenter/ucenter-member/getOrderMemberInfo/{memberId}")
    public UcenterMemberOrder getOrderMemberInfo(@PathVariable("memberId") String memberId);
}
