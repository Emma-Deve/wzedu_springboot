package com.wuze.eduservice.client;

import com.wuze.commonutils.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-26 12:25:02
 */
//注意（说明）：@Feign()的参数2的  fallback属性 是指定 Hystrix 熔断器 触发之后要执行的实现类，与远程调用实现类无关！
@Component
@FeignClient(name = "server-vod",fallback = VodClientImpl.class)
public interface VodClientInterface {

    //远程调用 vod 模块方法（feign）
    //根据视频id删除阿里云视频
    //@PathVariable要指定参数名称，否则报错
    //写全路径
    @ApiOperation(value = "根据视频id删除阿里云视频")
    @DeleteMapping("/eduvod/filevod/deleteAliyunVideo/{videoSourceId}")
    public R deleteAliyunVideo(@PathVariable("videoSourceId") String videoSourceId);

    //远程调用 vod 模块方法（feign）
    //删除阿里云多个视频（根据List集合）
    //可以加参数 @RequestParam("videoList") 表示需要一个List参数，并可以指定为缺省
    @ApiOperation(value = "批量删除阿里云视频")
    @DeleteMapping("/eduvod/filevod/deleteAliyunVideoBatch")
    public R deleteAliyunVideoBatch(@RequestParam(value = "videoList",required = false) List<String> videoList);

}
