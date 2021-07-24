package com.wuze.vod.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-25 16:48:02
 */
//跟OSS方法一样，采用这种定义方式，外面通过类名访问
@Component
public class ConstantVodUtils implements InitializingBean {

    @Value("${aliyun.vod.file.keyid}")
    private String keyId;

    @Value("${aliyun.vod.file.keysecret}")
    private String keySecret;

    //定义公开静态常量（外面用 类名.常量名 访问）
    public static String KEY_ID;
    public static String KEY_SECRET;

    @Override
    public void afterPropertiesSet() throws Exception {
        KEY_ID = keyId;
        KEY_SECRET = keySecret;
    }
}
