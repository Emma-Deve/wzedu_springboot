package com.wuze.eduorder.service;

import com.wuze.eduorder.entity.Order;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
public interface OrderService extends IService<Order> {

    //根据 课程id 和 用户id 生成订单（返回 订单编号）
    String createOrder(String courseId, String memberId);
}
