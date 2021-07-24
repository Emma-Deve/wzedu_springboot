package com.wuze.edustatistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.commonutils.R;
import com.wuze.edustatistics.client.UcenterClient;
import com.wuze.edustatistics.entity.StatisticsDaily;
import com.wuze.edustatistics.mapper.StatisticsDailyMapper;
import com.wuze.edustatistics.service.StatisticsDailyService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 网站统计日数据 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-02-06
 */
@Service
public class StatisticsDailyServiceImpl extends ServiceImpl<StatisticsDailyMapper, StatisticsDaily> implements StatisticsDailyService {

    //注入远程调用接口
    @Autowired
    private UcenterClient ucenterClient;

    //统计当天注册人数，并保存统计记录
    @Override
    public void getRegisterCountAndSave(String day) {
        //1、远程调用 ucenter 模块，获取当天注册人数
        R registerCount = ucenterClient.getRegisterCount(day);
        //因为ucenter那边是返回 R.ok().data("count",count);
        //是 map 结构，所以这里要取 map 的key
        //强转为 Integer 类型
        Integer count = (Integer)registerCount.getData().get("count");


        //2、添加到统计表中
        StatisticsDaily statisticsDaily = new StatisticsDaily();
        statisticsDaily.setRegisterNum(count);//统计数
        statisticsDaily.setDateCalculated(day);//统计日期

        //TODO：其他统计数据后期再完善，现在先暂时添加随机数保存（02/06）
        statisticsDaily.setCourseNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setLoginNum(RandomUtils.nextInt(100,200));
        statisticsDaily.setVideoViewNum(RandomUtils.nextInt(100,200));

        //先查询当天数据表有没有数据，如果有，先删除，再加新的，避免冗余
        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.eq("date_calculated",day);
        baseMapper.delete(wrapper);

        //再添加
        baseMapper.insert(statisticsDaily);
    }




    //图表显示，传入 类型、开始时间、截止时间。返回两部分数据
    @Override
    public Map<String, Object> showChart(String type, String begin, String end) {

        QueryWrapper<StatisticsDaily> wrapper = new QueryWrapper<>();
        wrapper.select("date_calculated",type);//统计的列：日期列、用户选择类型对应的列
        wrapper.between("date_calculated",begin,end);//统计时间范围

        List<StatisticsDaily> list = baseMapper.selectList(wrapper);//查询符合条件的数据，封装成list 集合

        //现在要求返回给前端两种数据，一种是日期数组，一种是数量数组
        //前端要求json 数组，所以后端需要用list才能搭配
        //遍历list，取出日期、时间，分别存放到两个不同的list中
        List<String> dateList = new ArrayList();
        List<Integer> numList = new ArrayList();
        for (int i = 0; i < list.size(); i++) {
            dateList.add(list.get(i).getDateCalculated());

            //TODO:判断传入的类型
            if(type.equals("register_num") ){
                numList.add(list.get(i).getRegisterNum());
            }
            if(type.equals("login_num") ){
                numList.add(list.get(i).getLoginNum());
            }
        }

        //把封装后的两个list集合放在map中，返回
        Map<String, Object> map = new HashMap<>();
        map.put("dateList",dateList);
        map.put("numList",numList);

        return map;
    }
}
