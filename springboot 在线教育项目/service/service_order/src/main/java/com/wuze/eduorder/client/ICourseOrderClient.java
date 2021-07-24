package com.wuze.eduorder.client;

import com.wuze.commonutils.OrderCommonClassVo.CourseOrder;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-03 16:48:31
 */
//注意（说明）：@Feign()的参数2的  fallback属性 是指定 Hystrix 熔断器 触发之后要执行的实现类，与远程调用实现类无关！
    //这里应该是不用写参数2的
@Component
//@FeignClient(name = "service-edu",fallback = OrderServiceImpl.class)  //参数1：调用接口的模块名称； 参数2：。。。
@FeignClient("service-edu") //修正后（2021/02/05）
public interface ICourseOrderClient {

    //注1：@PathVariable("xxx") 里面要写参数名称
    //注2：路径要写全路径
    @ApiOperation(value = "根据课程id查询课程信息（生成订单）")
    @GetMapping("/eduservice/edu-course/getOrderCourseById/{courseId}")
    public CourseOrder getOrderCourseById(@PathVariable("courseId") String courseId);
}
