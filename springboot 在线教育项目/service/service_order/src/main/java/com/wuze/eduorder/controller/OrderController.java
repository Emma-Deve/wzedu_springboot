package com.wuze.eduorder.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuze.commonutils.R;
import com.wuze.eduorder.entity.Order;
import com.wuze.eduorder.service.OrderService;
import com.wuze.eduorder.utils.JwtUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 订单 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
@RestController
@RequestMapping("/eduorder/order")
@CrossOrigin    //跨域
public class OrderController {

    @Autowired
    OrderService orderService;

    //生成订单（根据 课程id 和 用户id），返回订单id
    //注：用户id 可以从 http请求 那里获取token 然后解析出来（所以用户id 不用传入）
    @ApiOperation(value = "根据 ”课程id“ 和 ”用户id“ 生成订单")
    @PostMapping("makeOrder/{courseId}")
    public R makeOrder(@PathVariable String courseId, HttpServletRequest request){

        String memberId = JwtUtils.getMemberIdByJwtToken(request);
        //生成订单编号（传入 课程id 和 用户id）
        String orderNo = orderService.createOrder(courseId,memberId);

        return R.ok().data("orderNo",orderNo);//返回订单编号，而不是订单表的id！

    }


    //2、根据订单号查询订单信息
    //注意是 订单号，不是订单表id
    @ApiOperation(value = "根据订单号查询订单信息")
    @GetMapping("getOrderInfo/{OrderNo}")
    public R getOrderInfo(@PathVariable String OrderNo){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",OrderNo);
        Order order = orderService.getOne(wrapper);
        return R.ok().data("order",order);
    }


    //////////////////////////////

    //根据 课程id 和 用户id 查询订单表中的订单状态
    @ApiOperation(value = "根据 课程id 和 用户id 查询订单表中的订单状态")
    @GetMapping("OrderIsBuy/{courseId}/{memberId}")
    public boolean OrderIsBuy(@PathVariable String courseId,@PathVariable String memberId){
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        wrapper.eq("member_id",memberId);
        wrapper.eq("status",1);//status 为 1，表示已支付
        int count = orderService.count(wrapper);
/*        if(count>0){
            return true;
        }
        else {
            return false;
        }*/
        return count>0;//如果count>0，返回true，否则返回false
    }


}

