package com.wuze.eduorder.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.wxpay.sdk.WXPayUtil;
import com.wuze.eduorder.entity.Order;
import com.wuze.eduorder.entity.PayLog;
import com.wuze.eduorder.mapper.PayLogMapper;
import com.wuze.eduorder.service.OrderService;
import com.wuze.eduorder.service.PayLogService;
import com.wuze.eduorder.utils.HttpClient;
import com.wuze.servicebase.exceptionhandler.WzException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 支付日志表 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-02-03
 */
@Service
public class PayLogServiceImpl extends ServiceImpl<PayLogMapper, PayLog> implements PayLogService {

    @Autowired
    private OrderService orderService;

    //生成微信二维码（传入订单编号）
    @Override
    public Map createQRCodeInfo(String orderNo) {

        try{

            //1、根据订单号查询订单信息
            QueryWrapper<Order> wrapper = new QueryWrapper<>();
            wrapper.eq("order_no",orderNo);
            Order orderInfo = orderService.getOne(wrapper);

            //2、使用map 设置生成二维码需要参数（微信官方提供（固定格式））
            //商户号等信息教程提供（需要企业才能申请）
            Map<String, String> m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");   //关联的公众号appid
            m.put("mch_id", "1558950191");  //商户号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//微信提供的工具类，生成随机的订单识别码
            m.put("body", orderInfo.getCourseTitle());//课程标题
            m.put("out_trade_no", orderNo);//订单编号
            m.put("total_fee", orderInfo.getTotalFee().multiply(new BigDecimal("100")).longValue()+"");//总金额（精度转换）
            m.put("spbill_create_ip", "127.0.0.1");//TODO:如果部署到服务器，这个地址要改
            m.put("notify_url", "http://guli.shop/api/order/weixinPay/weixinNotify\n"); //回调地址
            m.put("trade_type", "NATIVE");

            //3、封装刚刚的map集合 发送 httpclient 请求到微信支付提供的固定地址，传递参数xml格式，client.post 发送请求（微信官方模板）
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            //设置成xml格式，传入 商户key，加密
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);//设置允许 https 请求格式
            client.post();//请求（使用post请求）


            //4、得到发生请求返回结果（得到的数据也是xml格式，需要手动转换成 map 格式再 return）
            String xmlContent = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(xmlContent);//返回的map包含二维码地址（主要）


            //5、封装其他我们所需的 map 数据（课程id、总金额、二维码操作状态码、二维码地址。。。），并return
            Map map = new HashMap<>();  //细节：因为待会课程总金额是BigDecimal格式，不是String，所以Map<String, Object>,但是返回类型是String，又冲突，所以干脆不写返回类型了
            map.put("out_trade_no",orderNo);//订单编号
            map.put("course_id",orderInfo.getCourseId());//课程id
            map.put("total_fee",orderInfo.getTotalFee());//总金额

            map.put("result_code",resultMap.get("result_code"));    //得到二维码操作状态码
            map.put("code_url",resultMap.get("code_url"));  //二维码地址（关键！）

            //微信支付二维码2小时过期，可采取2小时未支付取消订单
            //redisTemplate.opsForValue().set(orderNo, map, 120, TimeUnit.MINUTES);

            return map;

        }catch (Exception e){
            throw new WzException(20001,"生成微信二维码失败");
        }

    }

    ////////////////////////
    //查询订单支付状态（根据订单号）

    //1、根据订单号查询订单状态，返回map（里面包含微信官方封装的不同k-v）
    @Override
    public Map<String, String> queryPayStatus(String orderNo) {

        try {

            //1、封装map参数（微信官方模板）
            Map m = new HashMap<>();
            m.put("appid", "wx74862e0dfcf69954");//关联的公众号appid
            m.put("mch_id", "1558950191");//商户号
            m.put("out_trade_no", orderNo);//订单编号
            m.put("nonce_str", WXPayUtil.generateNonceStr());//微信提供的工具类，生成随机的订单识别码

            //2、发生httpclient（微信提供的固定地址），需要先转换成 xml 格式，再发起请求
            HttpClient client = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            client.setXmlParam(WXPayUtil.generateSignedXml(m,"T6m9iK73b0kn9g5v426MKfHQH7X8rKwb"));
            client.setHttps(true);
            client.post();

            //3、得到请求返回内容，返回的是 xml 格式，需要手动转换成 map 格式，再return
            String resultXMLContent = client.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(resultXMLContent);

            return resultMap;

        }catch (Exception e){
            throw new WzException(20001,"查询支付状态失败～");
        }

    }

    //////////////////////////////////////////////////////
    //////////////////////////////////////////////////////


    //2、添加记录到支付表（pay_log表），并更新订单表订单状态（order表）
    //map（里面包含微信官方封装的不同k-v）
    @Override
    public void updateOrderStatus(Map<String, String> payStatusMap) {
        //（1）订单表 order
        String orderNo = payStatusMap.get("out_trade_no");//订单编号
        //根据订单编号查询订单信息
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        wrapper.eq("order_no",orderNo);
        Order orderInfo = orderService.getOne(wrapper);

        //判断订单状态
        //如果是1，不更新
        if(orderInfo.getStatus().intValue() == 1){
            return;
        }
        //如果是0，则更新
        orderInfo.setStatus(1);
        orderService.updateById(orderInfo);

        //（2）支付表 pay_log
        PayLog payLog = new PayLog();
        payLog.setOrderNo(orderInfo.getOrderNo());//支付订单号
        payLog.setPayTime(new Date());
        payLog.setPayType(1);//支付类型
        payLog.setTotalFee(orderInfo.getTotalFee());//总金额(分)

        payLog.setTradeState(payStatusMap.get("trade_state"));//支付状态
        payLog.setTransactionId(payStatusMap.get("transaction_id"));//订单流水号
        payLog.setAttr(JSONObject.toJSONString(payStatusMap));//其他属性，将map转换成json格式set进去

        baseMapper.insert(payLog);//插入到支付日志表
    }
}
