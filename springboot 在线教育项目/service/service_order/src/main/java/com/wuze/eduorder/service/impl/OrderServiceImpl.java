package com.wuze.eduorder.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.commonutils.OrderCommonClassVo.CourseOrder;
import com.wuze.commonutils.OrderCommonClassVo.UcenterMemberOrder;
import com.wuze.commonutils.OrderNoUtil;
import com.wuze.eduorder.client.ICourseOrderClient;
import com.wuze.eduorder.client.IMemberOrderClient;
import com.wuze.eduorder.entity.Order;
import com.wuze.eduorder.mapper.OrderMapper;
import com.wuze.eduorder.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    @Autowired
    ICourseOrderClient iCourseOrderClient;  //远程调用接口（order）

    @Autowired
    IMemberOrderClient iMemberOrderClient;  //远程调用接口（member）



    //根据 课程id 和 用户id 生成订单（返回 订单编号）
    @Override
    public String createOrder(String courseId, String memberId) {
        //1、根据课程id 获取课程信息
        // （需要 远程调用 edu 模块）
        // 返回对象封装到一个公共类（其他模块才可以访问）
        CourseOrder courseOrder = iCourseOrderClient.getOrderCourseById(courseId);//用interface接口远程调用方法

        //2、根据用户id 获取用户信息（注：从url请求获取token，解析得到用户id）
        //（需要 远程调用 edu 模块）
        UcenterMemberOrder memberOrder = iMemberOrderClient.getOrderMemberInfo(memberId);//用interface接口远程调用方法

        Order order = new Order();

        order.setCourseCover(courseOrder.getCover());
        order.setCourseId(courseOrder.getId());
        order.setCourseTitle(courseOrder.getTitle());
        order.setTeacherName(courseOrder.getTeacherName());//课程讲师姓名
        order.setMemberId(memberOrder.getId());//用户id
        order.setMobile(memberOrder.getMobile());//用户手机号码
        order.setNickname(memberOrder.getNickname());//用户昵称
        order.setOrderNo(OrderNoUtil.getOrderNo());//订单编号（重要）（工具类随机生成（唯一））
        order.setTotalFee(courseOrder.getPrice());//（02/04补充）注：因为Order表的价格和courseOrder表价格的属性名称不一样，这里得手动传值

        order.setPayType(1);//设置支付方式（1为微信支付）
        order.setStatus(0); //设置支付状态（0为未支付）

        baseMapper.insert(order);//保存进数据库

        return order.getOrderNo();//返回订单编号（而不是订单表的id ！ ）

    }
}
