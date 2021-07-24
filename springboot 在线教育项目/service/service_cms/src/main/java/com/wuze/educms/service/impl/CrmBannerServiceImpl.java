package com.wuze.educms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.educms.entity.CrmBanner;
import com.wuze.educms.mapper.CrmBannerMapper;
import com.wuze.educms.service.CrmBannerService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-27
 */
@Service
public class CrmBannerServiceImpl extends ServiceImpl<CrmBannerMapper, CrmBanner> implements CrmBannerService {

    //查询所有banner
    //注：@Cacheable 是 springframework 下的依赖
    @Cacheable(value = "banner",key="'selectIndexList'")
    @Override
    public List<CrmBanner> getAllBanner() {
        //根据id进行降序排序
        QueryWrapper<CrmBanner> wrapperBanner = new QueryWrapper<>();
        wrapperBanner.orderByDesc("id");

        //拼接sql，取出前面2条数据（这样写有sql注入风险）
        wrapperBanner.last("limit 2");

        List<CrmBanner> bannerList = baseMapper.selectList(wrapperBanner);
        return bannerList;
    }
}
