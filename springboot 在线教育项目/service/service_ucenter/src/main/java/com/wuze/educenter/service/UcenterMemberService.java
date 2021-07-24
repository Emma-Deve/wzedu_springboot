package com.wuze.educenter.service;

import com.wuze.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.educenter.entity.vo.RegisterVo;

/**
 * <p>
 * 会员表 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-29
 */
public interface UcenterMemberService extends IService<UcenterMember> {

    String login(UcenterMember ucenterMember);

    void register(RegisterVo registerVo);

    UcenterMember getMemberByOpenid(String openId);

    //统计当天有多少用户注册
    int getRegisterCountDay(String day);
}
