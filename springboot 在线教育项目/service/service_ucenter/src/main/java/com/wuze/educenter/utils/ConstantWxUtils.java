package com.wuze.educenter.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-30 17:11:05
 */
@Component
public class ConstantWxUtils implements InitializingBean {

    @Value("${wx.open.app_id}")
    private String AppId;

    @Value("${wx.open.app_secret}")
    private String AppSecret;

    @Value("${wx.open.redirect_url}")
    private String RedirectUrl;


    public static String WX_OPEN_APP_ID;
    public static String WX_OPEN_APP_SECRET;
    public static String WX_OPEN_REDIRECT_URL;


    @Override
    public void afterPropertiesSet() throws Exception {
        WX_OPEN_APP_ID=AppId;
        WX_OPEN_APP_SECRET=AppSecret;
        WX_OPEN_REDIRECT_URL=RedirectUrl;
    }
}
