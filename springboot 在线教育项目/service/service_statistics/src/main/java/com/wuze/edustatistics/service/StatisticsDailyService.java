package com.wuze.edustatistics.service;

import com.wuze.edustatistics.entity.StatisticsDaily;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-02-06
 */
public interface StatisticsDailyService extends IService<StatisticsDaily> {

    //统计当天注册人数，并保存统计记录
    void getRegisterCountAndSave(String day);

    //图表显示，传入 类型、开始时间、截止时间。返回两部分数据
    Map<String, Object> showChart(String type, String begin, String end);
}
