package com.wuze.edustatistics.schedule;

import com.wuze.edustatistics.service.StatisticsDailyService;
import com.wuze.edustatistics.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-07 00:29:41
 */
@Component
public class ScheduledTask {

    @Autowired
    private StatisticsDailyService staService;

    //定时任务，调用统计注册人数方法
    //Spring 只支持6位，默认最后一位 年 为 当前年（本来有七位： 秒、分钟、小时、日、月、周、年）
    @Scheduled(cron = "0 0 1 * * ? ")
    public void ScheduledOfStatistics(){
        //在每一天的凌晨1点执行定时任务，把前一天的数据进行数据查询添加
        staService.getRegisterCountAndSave(DateUtil.formatDate(DateUtil.addDays(new Date(),-1)));
    }
}
