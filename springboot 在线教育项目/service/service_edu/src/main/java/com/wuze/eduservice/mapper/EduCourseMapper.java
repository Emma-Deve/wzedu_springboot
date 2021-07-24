package com.wuze.eduservice.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.vo.CourseFrontBaseInfoVo;
import com.wuze.eduservice.entity.vo.CoursePublishVo;

/**
 * <p>
 * 课程 Mapper 接口
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
public interface EduCourseMapper extends BaseMapper<EduCourse> {

    CoursePublishVo getPublishCourseInfo(String courseId);

    //根据课程id查询课程详情
    //多表（手写sql）
    CourseFrontBaseInfoVo getCourseBaseInfoMapper(String courseId);
}
