package com.wuze.eduorder.controller;


import com.wuze.commonutils.R;
import com.wuze.eduorder.service.PayLogService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 支付日志表 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/eduorder/pay-log")
@CrossOrigin    //跨域
public class PayLogController {

    @Autowired
    private PayLogService payLogService;

    //生成微信二维码（传入订单编号）
    @ApiOperation(value = "生成微信二维码（传入订单编号）")
    @PostMapping("createQRCode/{OrderNo}")
    public R createQRCode(@PathVariable String OrderNo){
        //Map没指定泛型，因为返回的有<String,Object> 和<String,String>
        Map codeMap = payLogService.createQRCodeInfo(OrderNo);
        //System.out.println("************生成微信二维码返回的map ==》 "+codeMap);
        return R.ok().data(codeMap);//返回map直接参数就一个map，不是k-v结构!!!（坑！）

    }


    //查询订单支付状态（根据订单号）
    @ApiOperation(value = "查询订单支付状态（根据订单号）")
    @GetMapping("queryPayStatus/{OrderNo}")
    public R queryPayStatus(@PathVariable String OrderNo){
        //1、根据订单号查询订单状态，返回map（里面包含微信官方封装的不同k-v）
        Map<String,String> payStatusMap = payLogService.queryPayStatus(OrderNo);
        //System.out.println("=========查询订单支付状态返回的map ==》 "+payStatusMap);
        //map为空
        if(payStatusMap == null){
            return R.error().message("支付出错了～");
        }
        //如果map不为空，则通过map获取订单状态
        //如果订单完成支付
        if(payStatusMap.get("trade_state").equals("SUCCESS")){
            //添加记录到支付表（pay_log表），并更新订单表订单状态（order表）
            //传入的 map（里面包含微信官方封装的不同k-v）
            payLogService.updateOrderStatus(payStatusMap);
            return R.ok().message("支付成功！");
        }


        //支付中，返回25000状态码（前端有 http response 拦截器）
        return R.ok().code(25000).message("支付中～");

    }

}

