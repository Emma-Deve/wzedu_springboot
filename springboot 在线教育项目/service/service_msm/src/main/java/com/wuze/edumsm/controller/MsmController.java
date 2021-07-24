package com.wuze.edumsm.controller;

import com.wuze.commonutils.R;
import com.wuze.edumsm.service.MsmService;
import com.wuze.edumsm.utils.RondomUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-28 18:40:41
 */
@RestController
@RequestMapping("/edumsm")
@CrossOrigin    //跨域
public class MsmController {

    @Autowired
    MsmService msmService;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @ApiOperation(value = "发送短信（aliyun）")
    @GetMapping("sendMsm/{phone}")   //get提交
    public R sendMsm(@PathVariable String phone){
        //从redis 缓存尝试读取验证码
        String code = redisTemplate.opsForValue().get(phone);
        //如果能从redis取到code值，直接将这个code返回
        if(!StringUtils.isEmpty(code)){
            return R.ok();
        }

        //如果取不到值，生成验证码然后用阿里云短信服务发送短信
        code = RondomUtil.getFourBitRandom();
        //将code值放到map中（k-v结构）
        Map<String,Object> param = new HashMap<>();
        param.put("code", code);
        //调用Service发送短信（绑定阿里云短信功能）（传递 验证码 和 手机号码）
        boolean flag = msmService.sendMessage(param,phone);

        //发送成功，将验证码存入redis缓存，并设置过期时间 5 min
        if(flag){
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            System.out.println("phone->code"+code);
            return R.ok();
        }
        else{
            return R.error().message("短信发送失败～");
        }
    }


}
