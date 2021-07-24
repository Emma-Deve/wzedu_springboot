package com.wuze.eduservice.controller;

import com.wuze.commonutils.R;
import org.springframework.web.bind.annotation.*;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-18 13:28:56
 */

@RestController
@RequestMapping("/eduservice/user")
@CrossOrigin //跨域
public class EduLoginController {

    //login
    //返回token
    //Post 提交方式
    @PostMapping("login")
    public R login(){
        return R.ok().data("token","admin");
    }

    //info
    //返回 roles、name、avatar值
    //Get 提交方式
    @GetMapping("info")
    public R getinfo(){
        return R.ok().data("roles","[admin]").data("name","admin").data("avatar","https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
    }
}
