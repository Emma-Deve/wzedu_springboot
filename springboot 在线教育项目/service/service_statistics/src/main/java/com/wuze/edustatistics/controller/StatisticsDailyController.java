package com.wuze.edustatistics.controller;


import com.wuze.commonutils.R;
import com.wuze.edustatistics.service.StatisticsDailyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-02-06
 */
@RestController
@RequestMapping("/edustatistics/statistics-daily")
@CrossOrigin    //跨域
public class StatisticsDailyController {

    @Autowired
    private StatisticsDailyService statisticsDailyService;

    //统计当天注册人数，并保存统计记录
    @ApiOperation(value = "统计当天注册人数，并保存统计记录")
    @PostMapping("getRegisterCountAndSave/{day}")
    public R getRegisterCountAndSave(@PathVariable String day){
        statisticsDailyService.getRegisterCountAndSave(day);
        return R.ok();
    }


    //图表显示，传入 类型、开始时间、截止时间。返回两部分数据
    //1、日期json数组
    //2、数量json数组
    @ApiOperation(value = "图表显示")
    @GetMapping("showChart/{type}/{begin}/{end}")
    public R showChart(@PathVariable String type,@PathVariable String begin,@PathVariable String end){
        //将两部分的数组封装到map中返回，到时候前端根据map的list的key就能取到json数组
        Map<String,Object> map = statisticsDailyService.showChart(type,begin,end);
        return R.ok().data(map);
    }
}

