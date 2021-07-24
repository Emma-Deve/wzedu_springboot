package com.wuze.servicebase.exceptionhandler;

import com.wuze.commonutils.R;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author wuze
 * @desc ...
 * @date 2021-01-05 15:37:02
 */
@ControllerAdvice
public class GlobalExceptionHandler {

   //指定出现什么异常执行这个方法
    @ExceptionHandler(Exception.class)
    @ResponseBody   //为了返回数据
    public R err(Exception e){ //注意这个R是自定义的包里面的R（需要手动到pom添加包，（ R 在common_utils包中）），不是baomidou的
        e.printStackTrace();//打印出异常
        return R.error().message("执行了全局异常处理~");
    }


    //自定义异常类
    @ExceptionHandler(WzException.class)
    @ResponseBody   //为了返回数据
    public R err(WzException e){ //注意这个R是自定义的包里面的R（需要手动到pom添加包，（ R 在common_utils包中）），不是baomidou的
        e.printStackTrace();
        return R.error().code(e.getWzCode()).message(e.getWzMsg());
    }

}
