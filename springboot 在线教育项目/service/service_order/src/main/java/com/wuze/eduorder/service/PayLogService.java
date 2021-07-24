package com.wuze.eduorder.service;

import com.wuze.eduorder.entity.PayLog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 支付日志表 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
public interface PayLogService extends IService<PayLog> {

    //生成微信二维码（传入订单编号）
    Map createQRCodeInfo(String orderNo);

    //1、根据订单号查询订单状态，返回map（里面包含微信官方封装的不同k-v）
    Map<String, String> queryPayStatus(String orderNo);

    //2、添加记录到支付表（pay_log表），并更新订单表订单状态（order表）
    void updateOrderStatus(Map<String, String> payStatusMap);
}
