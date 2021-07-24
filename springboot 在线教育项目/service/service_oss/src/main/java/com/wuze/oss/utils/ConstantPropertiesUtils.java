package com.wuze.oss.utils;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 01:17:44
 */
//因为下面的属性都是private，就是赋值了外面也使用不了，
// 所以要让这个类实现一个接口，具体流程如下：
// 启动项目时，这个类交给Spring管理（因为注解@Component），
// 当这个类中的属性读取到配置文件的相关值后，
// 这个接口方法就会执行，我们让这些私有属性的值赋给一个公开的静态常量，
// 就能实现让这些属性的值外面能访问到。（外面用 类名.常量名 访问）
//因为这些信息涉及安全性问题，所以不能直接将属性设置为public，而是要加一层！
    //但其实好像直接用 get 方法也行？
@Component
public class ConstantPropertiesUtils implements InitializingBean {
    //读取配置文件的内容
    @Value("${aliyun.oss.file.endpoint}")
    private String endPoint;

    @Value("${aliyun.oss.file.keyid}")
    private String keyId;

    @Value("${aliyun.oss.file.keysecret}")
    private String keySecret;

    @Value("${aliyun.oss.file.bucket}")
    private String bucket;

    //定义公开静态常量（外面用 类名.常量名 访问）
    public static String END_POINT;
    public static String KEY_ID;
    public static String KEY_SECRET;
    public static String BUCKET;


    @Override
    public void afterPropertiesSet() throws Exception {
        END_POINT=endPoint;
        KEY_ID=keyId;
        KEY_SECRET=keySecret;
        BUCKET=bucket;
    }
}
