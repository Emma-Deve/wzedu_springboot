package com.wuze.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.commonutils.OrderCommonClassVo.CourseOrder;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.vo.*;

import java.util.Map;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
public interface EduCourseService extends IService<EduCourse> {

    //添加课程信息
    String saveCourseInfo(CourseInfoVo courseInfoVo);

    //根据课程id查询课程信息
    CourseInfoVo getCourseById(String courseId);

    ////修改课程信息
    void updateCourseInfo(CourseInfoVo courseInfoVo);


    CoursePublishVo getPublishCourseInfo(String courseId);

    void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery);

    void removeCourse(String courseId);

    //前台页面，条件带分页查询课程列表
    Map<String, Object> getCourseVoPageMap(Page<EduCourse> page, CourseFrontVo courseFrontVo);

    //课程基本信息(路径传入课程id)
    //多表查询
    CourseFrontBaseInfoVo getCourseBaseInfo(String courseId);


    //////////////////
    //生成订单

    //根据课程id获取课程信息（生成订单）
    CourseOrder getOrderCourseById(String courseId);
}
