package com.wuze.educenter.mapper;

import com.wuze.educenter.entity.UcenterMember;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 会员表 Mapper 接口
 * </p>
 *
 * @author wuze
 * @since 2021-01-29
 */
public interface UcenterMemberMapper extends BaseMapper<UcenterMember> {

    //统计当天有多少用户注册（在xml文件手写sql，绑定这个方法）
    int getRegisterCountDay(String day);
}
