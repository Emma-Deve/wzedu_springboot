package com.wuze.educenter.controller;

import com.google.gson.Gson;
import com.wuze.educenter.entity.UcenterMember;
import com.wuze.educenter.service.UcenterMemberService;
import com.wuze.educenter.utils.ConstantWxUtils;
import com.wuze.educenter.utils.HttpClientUtils;
import com.wuze.educenter.utils.JwtUtils;
import com.wuze.servicebase.exceptionhandler.WzException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-30 17:15:57
 */
@Controller //返回页面，所以用这个（而不用 @RestController）
@RequestMapping("/api/ucenter/wx")
@CrossOrigin    //跨域
public class WxApiController {

    @Autowired
    private UcenterMemberService memberService;

    //微信扫码（获取扫码人信息）
    //这个方法在实际工作不使用，现在只是测试使用
    //提供的服务器接口定义了接口路径为callback，所以不要再拼接路径参数了
    @ApiOperation(value = "获取扫码人信息（测试使用）")
    @GetMapping("callback")
    public String callback(String code,String state){
        //1、第一步：执行 callback 方法，获取 code 值
//        System.out.println("code ==> "+code);
//        System.out.println("state ==> "+state);

        try {
            //2、拿着code值，请求微信提供的固定地址，获取两个值（access_token、openid）
            //利用 httpclient、HashMap+gson 技术
            //向认证服务器发送请求换取access_token
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数：id、密钥、code值
            String accessTokenUrl  = String.format(baseAccessTokenUrl, ConstantWxUtils.WX_OPEN_APP_ID, ConstantWxUtils.WX_OPEN_APP_SECRET, code);
            //使用 httpclient 工具类 发送请求（获得带有accessToken等信息的json字符串）
            String accessTokenResult = HttpClientUtils.get(accessTokenUrl);
            //取出 字符串 中的 access_token 和 openid 值（利用 HashMap+gson）
            Gson gsonOfAccessToken =new Gson();
            //将json字符串转换成 hashmap 格式（k-v）
            HashMap mapAccessToken  = gsonOfAccessToken.fromJson(accessTokenResult, HashMap.class);
            //获取 这两个字段值
            String accessToken = (String)mapAccessToken.get("access_token");
            String openId = (String)mapAccessToken.get("openid");

            //3、根据 openid 值判断用户是否存在（如果是新用户，才执行下面的“获取用户信息完成注册”操作）
            //如果是老用户，数据库已经有信息，直接查询出来并跳转到首页，就完成登录（也有显示用户信息）
            UcenterMember member = memberService.getMemberByOpenid(openId);
            //新用户
            if(member == null){
                //获取用户信息，并自动完成注册
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=%s" +
                "&openid=%s";

                //拼接两个参数：access_token、openid
                String UserInfoUrl = String.format(baseUserInfoUrl,accessToken,openId);
                //同理，httpclient+gson 获取信息
                //请求拼接好的地址，返回json格式字符串
                String UserInfoUrlResult = HttpClientUtils.get(UserInfoUrl);
                Gson gsonOfUserInfo = new Gson();
                HashMap mapUserInfo = gsonOfUserInfo.fromJson(UserInfoUrlResult, HashMap.class);
                String nickname = (String) mapUserInfo.get("nickname");//获取昵称
                String headimgurl = (String) mapUserInfo.get("headimgurl");//获取头像


                member = new UcenterMember();
                //4、向数据库插入一条数据（完成新用户注册）
                member.setNickname(nickname);
                member.setAvatar(headimgurl);
                member.setOpenid(openId);
                memberService.save(member);//注册

            }

            //5、利用jwt规则生成token字符串，重定向到首页时在链接带上
            String jwtToken = JwtUtils.getJwtToken(member.getId(), member.getNickname());

            return "redirect:http://localhost:3000?token="+jwtToken;
            
        }catch (Exception e){
            throw new WzException(20001,"微信扫码出错");
        }
        
    }





    //因为不需要返回数据，所以不用统一返回接口R
    @ApiOperation(value = "微信扫码登录")
    @GetMapping("login")
    public String wxLogin(){
        //官方提供参考链接模板：
        //       https://open.weixin.qq.com/connect/qrconnect?appid=APPID
        //        &redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect

        //在这统一定义，%s相当于占位符？，待会直接传入
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect"+
                "?appid=%s"+
                "&redirect_uri=%s"+
                "&response_type=code"+
                "&scope=snsapi_login"+
                "&state=%s"+
                "#wechat_redirect";

        //对redirect_uri 进行urlEncode 编码（官方要求）
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
        }catch (Exception e){
            e.printStackTrace();
        }

        //state 属性：防止csrf攻击（跨站请求伪造攻击）
        //String state = UUID.randomUUID().toString().replaceAll("-", "");//一般情况下会使用一个随机数

        String url = String.format(baseUrl, ConstantWxUtils.WX_OPEN_APP_ID, redirectUrl, "wz_OnlineEdu");

        return "redirect:"+url;
    }

}
