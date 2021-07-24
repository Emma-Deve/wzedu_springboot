package com.wuze.commonutils;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-04 17:19:02
 */
//统一返回结果类

@Data
public class R {
    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private Map<String, Object> data = new HashMap<String, Object>();



    ///
    //注意：私有构造方法（避免别人new）（现在别人只能使用我们下面定义的访问）
    private R(){}


    ///////////
    //成功- 静态方法（通过类名可以调用到）
    public static R ok(){
        R r = new R();

        //R类的三个属性
        r.setSuccess(true);
        r.setCode(ResultCode.SUCCESS);//调用接口中常量
        r.setMessage("成功");

        return r;
    }
    ////////////



    /////////
    //失败- 静态方法（通过类名可以调用到）
    public static R error(){
        R r = new R();

        //R类的三个属性
        r.setSuccess(false);
        r.setCode(ResultCode.ERROR);//调用接口中常量
        r.setMessage("失败");

        return r;
    }
    ////////



    ///////////////////////////////////////////////////////
    //下面是对四个属性的设置封装，方便调用时使用链式编程。
    //下面都是return this，this就是指当前调用方法的对象，
    //一个方法返回了this（当前对象），所以“当前对象”又能够调用别人，实现链式编程！

    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

    public R message(String message){
        this.setMessage(message);
        return this;
    }

    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    public R data(String key, Object value){
        this.data.put(key, value);
        return this;
    }

    public R data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}