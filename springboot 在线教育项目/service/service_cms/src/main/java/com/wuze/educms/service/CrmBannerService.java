package com.wuze.educms.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.educms.entity.CrmBanner;

import java.util.List;

/**
 * <p>
 * 首页banner表 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-27
 */
public interface CrmBannerService extends IService<CrmBanner> {

    List<CrmBanner> getAllBanner();
}
