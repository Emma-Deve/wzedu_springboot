package com.wuze.educenter.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.commonutils.JwtUtils;
import com.wuze.commonutils.MD5;
import com.wuze.educenter.entity.UcenterMember;
import com.wuze.educenter.entity.vo.RegisterVo;
import com.wuze.educenter.mapper.UcenterMemberMapper;
import com.wuze.educenter.service.UcenterMemberService;
import com.wuze.servicebase.exceptionhandler.WzException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * <p>
 * 会员表 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-29
 */
@Service
public class UcenterMemberServiceImpl extends ServiceImpl<UcenterMemberMapper, UcenterMember> implements UcenterMemberService {

    @Autowired
    RedisTemplate<String,String> redisTemplate;


    //根据手机号和密码登录
    //MD5 加密，要根据加密后的密码去比对数据库
    @Override
    public String login(UcenterMember ucenterMember) {
        //获取传入的对象的 手机号 和 密码
        String mobile=ucenterMember.getMobile();
        String pwd = ucenterMember.getPassword();

        //如果有一个为空，就拒绝登录
        if(mobile.isEmpty() || pwd.isEmpty()){
            throw new WzException(20001,"手机号或密码为空，拒绝登录");
        }

        //根据手机号和密码查询
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",mobile);
        UcenterMember member = baseMapper.selectOne(wrapper);
        String userMobile = member.getMobile(); //获取查出的对象的手机号
        //如果为空，说明数据库没有这个手机号，不给它登录
        if(StringUtils.isEmpty(userMobile)){
            throw new WzException(20001,"手机号无效，拒绝登录");
        }
        //走到这，说明有手机号
        //所以来判断密码是否正确
        //MD5加密后比对（MD5工具类，写在 common_utils 模块,因为service模块的pom有导入common模块,所以可以联动）
        String UserPwdDB = member.getPassword();  //已加密数据（数据库中）（这个对象是根据手机号查询出来的，现在在进一步匹配密码）
        String UserPwdInput = MD5.encrypt(pwd);   //手动加密（页面输入）（能到这里，说明pwd有值）

        //如果密码不匹配，拒绝登录
        if(!UserPwdDB.equals(UserPwdInput)){
            throw new WzException(20001,"密码不正确，拒绝登录");
        }

        //到这里，手机号和密码都正确
        //生成token（利用 jwt 规则）（根据id和昵称nickname生成token字符串）
        String token = JwtUtils.getJwtToken(member.getId(),member.getNickname());

        //返回token字符串
        return token;
    }


    //注册
    @Override
    public void register(RegisterVo registerVo) {
        //获取用户输入的值
        String phone=registerVo.getMobile();
        String password=registerVo.getPassword();
        String nickName=registerVo.getNickname();
        //Integer sex=registerVo.getSex();
        Integer age=registerVo.getAge();
        String code=registerVo.getCode();

        //判断是否为空
        if(phone.isEmpty() || password.isEmpty() || nickName.isEmpty() ||
        age==0 || code.isEmpty()){
            throw new WzException(20001,"有字段为空，无法完成注册");
        }


        //如果所有属性都有值，判断是否正确

        //先比对验证码
        //根据key（手机号）获取验证码
        //传入phone手机号码，参数名是什么无影响
        String CodeFromRedis = redisTemplate.opsForValue().get(phone);
        System.out.println("phone == >"+phone);


        System.out.println("code="+code);
        System.out.println("CodeFromRedis="+CodeFromRedis);

        //判断redis缓存中这个手机号的验证码 与 用户页面输入的验证码 是否一致
        if(!CodeFromRedis.equals(code)){
            //不一致

            throw new WzException(20001,"验证码错误，无法完成注册");
        }

        //到这里，验证码正确
        //接下来检测手机号是否注册过
        //注意这里wrapper使用 UcenterMember 实体类 构建的
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("mobile",phone);
        Integer count = baseMapper.selectCount(wrapper);

        //测试注册功能时注释掉
        if(count>0){
            //注册过
            throw new WzException(20001,"该手机号已被注册");
        }

        //到这里，通关，将数据保存到UcenterMember实体类（数据表）
        UcenterMember ucenterMember = new UcenterMember();


        ucenterMember.setMobile(phone);
        ucenterMember.setPassword(MD5.encrypt(password));//MD5加密
        ucenterMember.setNickname(nickName);
        //ucenterMember.setSex(sex);
        ucenterMember.setAge(age);
        ucenterMember.setIsDisabled(false);//不禁用此帐号（默认也是false）
        ucenterMember.setAvatar("https://onlineedu-wz.oss-cn-hangzhou.aliyuncs.com/2021/01/22/3fc2259071fc4b30955d97407cc7697b05.jpg");//设置默认头像

        //保存
        baseMapper.insert(ucenterMember);
    }

    //根据openid查找用户（微信扫码登录模块需要）
    @Override
    public UcenterMember getMemberByOpenid(String openId) {
        QueryWrapper<UcenterMember> wrapper = new QueryWrapper<>();
        wrapper.eq("openid",openId);
        UcenterMember member = baseMapper.selectOne(wrapper);//因为openid是每个用户唯一的，所以selectOne即可
        return member;
    }

    //统计当天有多少用户注册
    @Override
    public int getRegisterCountDay(String day) {
        return baseMapper.getRegisterCountDay(day);//调用mapper中的自定义方法（xml文件手写sql）
    }
}
