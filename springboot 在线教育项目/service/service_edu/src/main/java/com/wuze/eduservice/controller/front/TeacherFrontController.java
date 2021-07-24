package com.wuze.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuze.commonutils.R;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.EduTeacher;
import com.wuze.eduservice.service.EduCourseService;
import com.wuze.eduservice.service.EduTeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-31 14:36:17
 */
@RestController
@RequestMapping("/eduservice/index")
@CrossOrigin
public class TeacherFrontController {
    @Autowired
    private EduTeacherService teacherService;

    @Autowired
    private EduCourseService courseService;

    //分页查询名师列表（每页8条数据）
    @ApiOperation(value = "分页查询讲师列表")
    @GetMapping("getTeacherPage/{current}/{limit}")
    public R getTeacherPage(@PathVariable long current,@PathVariable long limit){
        Page<EduTeacher> teacherPage = new Page<>(current,limit);
        Map<String,Object> teacherPageMap = teacherService.selectTeacherPage(teacherPage);
        return R.ok().data(teacherPageMap);
    }


    //讲师详情功能
    //1、根据讲师id查询讲师基本信息
    //2、根据讲师id查询讲师所讲课程
    @ApiOperation(value = "讲师详情功能")
    @GetMapping("getTeacherFrontInfo/{teacherId}")
    public R getTeacherFrontInfo(@PathVariable String teacherId){

        //1、根据讲师id查询讲师基本信息
        EduTeacher teacherInfo = teacherService.getById(teacherId);

        //2、根据讲师id查询讲师所讲课程
        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_id",teacherId);//查询teacherId 对应的课程
        List<EduCourse> courseList = courseService.list(wrapper);

        //将 讲师对象 和 讲师对应的课程列表返回
        return R.ok().data("teacherInfo",teacherInfo).data("courseList",courseList);
    }



}
