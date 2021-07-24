package com.wuze.educms.controller;

import com.wuze.commonutils.R;
import com.wuze.educms.entity.CrmBanner;
import com.wuze.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-27 09:51:07
 */

//前台
@RestController
@RequestMapping("/educms/frontbanner")
@CrossOrigin
public class BannerFrontController {

    @Autowired
    CrmBannerService bannerService;



    //查询所有banner（List）（前台）
    @ApiOperation(value = "查询所有banner")
    @GetMapping("getAllBanner")
    public R getAllBanner(){
        List<CrmBanner> allBanner = bannerService.getAllBanner();
        return R.ok().data("allBanner",allBanner);  //返回所有 banner 的list列表
    }
}
