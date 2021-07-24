package com.wuze.edumsm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.wuze.edumsm.service.MsmService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-28 18:44:03
 */
@Service
public class MsmServiceImpl  implements MsmService {

    //发送短信
    @Override
    public boolean sendMessage(Map<String, Object> param, String phone) {

        //如果传入的手机号为空，直接返回false
        if(phone.isEmpty()){
            return false;
        }

        //固定（系统方法）（绑定基本信息）
        DefaultProfile profile = DefaultProfile.getProfile("default", "LTAI4GAL4KRB17VWSeVv2s5g","2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7" );
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        //固定，设置基本信息
        request.setSysMethod(MethodType.POST);  //这里固定为 POST（区别controler方法用get发送短信）
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        //固定方法，设置个人基本信息（注：参数1固定名称，不要修改！）
        //JSONObject (fastjson包 方法)，因为需要转换成 json 格式才能传输
        request.putQueryParameter("PhoneNumbers",phone);
        request.putQueryParameter("SignName","个人搭建学习设计展示");//签名名称（不能随便写！）
        request.putQueryParameter("TemplateCode","SMS_210770681");//模板名称
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));

        try {
            //固定
            CommonResponse response = client.getCommonResponse(request);
            return response.getHttpResponse().isSuccess();

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
