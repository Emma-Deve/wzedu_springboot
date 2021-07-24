package com.wuze.eduservice.controller.front;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuze.commonutils.R;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.EduTeacher;
import com.wuze.eduservice.service.EduCourseService;
import com.wuze.eduservice.service.EduTeacherService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-27 10:23:15
 */
@RestController
@RequestMapping("/eduservice/index")
@CrossOrigin
public class IndexController {
    @Autowired
    EduCourseService courseService;

    @Autowired
    EduTeacherService teacherService;

    //查询前8条课程，查询前4名讲师
    //不严谨：id 是 随机生成的，没有顺序意义可言，这里用id不是很好，但是好像也没有更好的排序字段了（课程说不定还能用访问量来排序...）
    @ApiOperation(value = "查询前8条课程，查询前4名讲师")
    @GetMapping("getHostCourseAndTeacher")
    public R getHostCourseAndTeacher(){
        //1、查询前8条课程
        QueryWrapper<EduCourse> wrapperCourse = new QueryWrapper<>();
        wrapperCourse.orderByDesc("id");    //按id排序
        wrapperCourse.last("limit 8");  //取出前8条数据
        List<EduCourse> eightCourselist = courseService.list(wrapperCourse);

        //2、查询前4名讲师
        QueryWrapper<EduTeacher> wrapperTeacher = new QueryWrapper<>();
        wrapperTeacher.orderByDesc("id");
        wrapperTeacher.last("limit 4");
        List<EduTeacher> fourTeacherlist = teacherService.list(wrapperTeacher);

        //返回前8条课程、前4名讲师（两个List列表）
        return R.ok().data("eightCourselist",eightCourselist).data("fourTeacherlist",fourTeacherlist);

    }
}
