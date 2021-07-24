package com.wuze.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.eduservice.entity.EduCourseDescription;
import com.wuze.eduservice.mapper.EduCourseDescriptionMapper;
import com.wuze.eduservice.service.EduCourseDescriptionService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 课程简介 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
// 注：课程简介 的 controller 不需要单独操作，把它放在课程里面操作即可
@Service
public class EduCourseDescriptionServiceImpl extends ServiceImpl<EduCourseDescriptionMapper, EduCourseDescription> implements EduCourseDescriptionService {

    //根据课程id删除课程简介
    @Override
    public void removeDescriptionByCourseId(String courseId) {
        QueryWrapper<EduCourseDescription> wrapper = new QueryWrapper<>();
        //注意：课程简介表的课程id字段名称是 “id”，不是 “course_id”
        wrapper.eq("id",courseId);
        baseMapper.delete(wrapper);
    }
}
