package com.wuze.educenter.controller;


import com.wuze.commonutils.JwtUtils;
import com.wuze.commonutils.OrderCommonClassVo.UcenterMemberOrder;
import com.wuze.commonutils.R;
import com.wuze.educenter.entity.UcenterMember;
import com.wuze.educenter.entity.vo.RegisterVo;
import com.wuze.educenter.service.UcenterMemberService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-01-29
 */
@RestController
@RequestMapping("/educenter/ucenter-member")
@CrossOrigin    //跨域
public class UcenterMemberController {

    @Autowired
    UcenterMemberService memberService;

    //登录（手机号 + 密码）
    //传入一个对象
    //Post 提交，对应 @RequestBody
    @ApiOperation(value = "登录")
    @PostMapping("login")
    public R login(@RequestBody UcenterMember ucenterMember){

        String token = memberService.login(ucenterMember);
        return R.ok().data("token",token);
    }


    //注册
    //传入 vo 类 对象
    //Post提交，对应@RequestBody
    @ApiOperation(value = "注册")
    @PostMapping("register")
    public R register(@RequestBody RegisterVo registerVo){
        memberService.register(registerVo);
        return R.ok();
    }

    //根据浏览器url的token字符串获取用户信息
    @ApiModelProperty(value = "根据token获取用户信息")
    @GetMapping("getMemberInfo")
    public R getMemberInfo(HttpServletRequest request){
        //根据浏览器url的token字符串获取id
        String id = JwtUtils.getMemberIdByJwtToken(request);
        //再根据id获取用户所有信息（对象）
        UcenterMember member = memberService.getById(id);

        return R.ok().data("memberInfo",member);
    }


    /////////////////////////生成订单/////////////////////////////////


    //根据用户id获取用户信息
    //返回公共类 UcenterMemberOrder （不要返回R，方便待会在其他后端接口取值）
    //TODO:Post 提交（用Get提交？）
    @ApiModelProperty(value = "根据用户id获取用户信息")
    @PostMapping("getOrderMemberInfo/{memberId}")
    public UcenterMemberOrder getOrderMemberInfo(@PathVariable String memberId){
        //再根据id获取用户所有信息（对象）
        UcenterMember member = memberService.getById(memberId);
        UcenterMemberOrder memberOrder = new UcenterMemberOrder();
        BeanUtils.copyProperties(member,memberOrder);
        return memberOrder;
    }


    /////////////////////////////////////////////////////////////////////
    //////////////////////// 统计模块 ////////////////////////////////////
    //统计当天有多少用户注册
    @ApiModelProperty(value = "统计当天有多少用户注册")
    @PostMapping("getRegisterCount/{day}")
    public R getRegisterCount(@PathVariable String day){
        int count = memberService.getRegisterCountDay(day);
        //System.out.println("uncenter 模块 ==》count==》"+count);
        return R.ok().data("count",count);
    }
}

