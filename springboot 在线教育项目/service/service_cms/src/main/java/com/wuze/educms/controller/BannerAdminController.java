package com.wuze.educms.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuze.commonutils.R;
import com.wuze.educms.entity.CrmBanner;
import com.wuze.educms.service.CrmBannerService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 首页banner表 后台（增删改查）
 * </p>
 *
 * @author wuze
 * @since 2021-01-27
 */
@RestController
@RequestMapping("/educms/adminbanner")
@CrossOrigin    //跨域
public class BannerAdminController {

    @Autowired
    CrmBannerService bannerService;

    //添加
    @ApiOperation(value = "添加banner")
    @PostMapping("addBanner")
    public R addBanner(@RequestBody CrmBanner crmBanner){
        bannerService.save(crmBanner);
        return R.ok();
    }

    //修改
    @ApiOperation(value = "修改banner")
    @PostMapping("updateBanner")
    public R updateBanner(@RequestBody CrmBanner crmBanner){
        bannerService.updateById(crmBanner);
        return R.ok();
    }

    //查询所有banner（List）（分页）（后台）
    //currentPage 当前页
    //limit 每页条数
    @ApiOperation(value = "查询所有banner（分页）")
    @GetMapping("getPageBanner/{currentPage}/{limit}")
    public R getPageBanner(@PathVariable Long currentPage,Long limit){
        //Page 是 baomidou 的，不要引入 springframework 的
        Page<CrmBanner> bannerPage = new Page<>(currentPage,limit);

        bannerService.page(bannerPage, null);
        List<CrmBanner> records = bannerPage.getRecords();//获得记录数List集合
        long total = bannerPage.getTotal();//获取总数

        return R.ok().data("bannerPage",records).data("total",total);  //返回所有 banner 的list列表
    }

    //删除banner（id）
    @ApiOperation(value = "根据id删除banner")
    @DeleteMapping("deleteBanner/{bannerId}")
    public R deleteBanner(@PathVariable String bannerId){
        bannerService.removeById(bannerId);
        return R.ok();
    }


}

