package com.wuze.eduservice.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuze.commonutils.OrderCommonClassVo.CourseOrder;
import com.wuze.commonutils.R;
import com.wuze.eduservice.client.VodClientInterface;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.vo.CourseInfoVo;
import com.wuze.eduservice.entity.vo.CoursePublishVo;
import com.wuze.eduservice.entity.vo.CourseQuery;
import com.wuze.eduservice.service.EduCourseService;
import com.wuze.eduservice.service.EduVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/edu-course")
@CrossOrigin //跨域
public class EduCourseController {

    @Autowired
    VodClientInterface vodClientInterface;

    @Autowired
    EduVideoService eduVideoService;

    @Autowired
    EduCourseService eduCourseService;




    //添加课程信息
    @ApiOperation(value = "添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        String id = eduCourseService.saveCourseInfo(courseInfoVo);
        return R.ok().data("courseId",id);//返回课程id
    }

    //根据课程id查询课程信息
    @ApiOperation(value = "根据课程id查询课程信息")
    @GetMapping("getCourseById/{courseId}")
    public R getCourseById(@PathVariable String courseId){
        CourseInfoVo courseById = eduCourseService.getCourseById(courseId);
        return R.ok().data("courseById",courseById);//返回课程信息（含课程简介）
    }


    //修改课程信息
    //第二步返回上一步时的操作
    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoVo courseInfoVo){
        eduCourseService.updateCourseInfo(courseInfoVo);
        return R.ok();
    }

    //课程发布页面（获取所有相关信息显示）
    @ApiOperation(value = "课程发布页面信息显示")
    @GetMapping("getPublishCourseInfo/{courseId}")
    public R getPublishCourseInfo(@PathVariable String courseId){
        CoursePublishVo coursePublishInfo = eduCourseService.getPublishCourseInfo(courseId);
        return R.ok().data("coursePublishInfo",coursePublishInfo);
    }

    //发布课程
    //修改课程状态
    @ApiOperation(value = "发布课程")
    @PostMapping("publishCourse/{courseId}")
    public R publishCourse(@PathVariable String courseId){
        EduCourse course = eduCourseService.getById(courseId);
        course.setStatus("Normal");//设置为发布状态
        eduCourseService.updateById(course);
        return R.ok();
    }


    ///////////////////////////////////
    //////////////////////////////////

    //课程列表

    //1、分页查询
    //currentpage：当前页码
    //limit：每页记录数
    @ApiOperation(value = "分页查询")
    @GetMapping("pageCourse/{currentpage}/{limit}")
    public R pageList( @PathVariable Long currentpage,
                       @PathVariable Long limit){

        Page<EduCourse> pageCourse=new Page<>(currentpage,limit);

        //调用方法实现分页
        //调用方法的时候，底层封装，把分页所有数据封装pageCourse对象里面
        eduCourseService.page(pageCourse,null);

        List<EduCourse> records=pageCourse.getRecords();//返回所有记录
        long total = pageCourse.getTotal();//返回总记录条数

        //也可以写成
   /* Map map = new HashMap<>();
    map.put("total",total);
    map.put("rows",records);
    return R.ok().data(map);*/

        return R.ok().data("total",total).data("rows",records);

    }


    //2、多条件组合查询 带分页
    //currentpage：当前页码
    //limit：每页记录数
    //courseQuery 查询条件（模糊查询）
    //注：改成@RequestBody 需要使用 Post 提交（RequestBody表示使用json传递数据，我们需要把json数据封装到对应对象里面）
    @ApiOperation(value = "多条件组合查询（带分页）")
    @PostMapping("pageCourseCondition/{currentpage}/{limit}")
    public R pageCourseCondition( @PathVariable Long currentpage,
                                   @PathVariable Long limit,
                                   @RequestBody(required = false) CourseQuery courseQuery){

        Page<EduCourse> pageCourse=new Page<>(currentpage,limit);


        //在分页查询基础上增加teacherQuery参数给wrapper作为查询条件
        //在自定义方法pageQuery()中配置wrapper，间接调用 ".page()"
        eduCourseService.pageQuery(pageCourse,courseQuery);


        List<EduCourse> records=pageCourse.getRecords();//返回所有记录
        long total = pageCourse.getTotal();//返回总记录条数

        return R.ok().data("total",total).data("rows",records);

    }



    //删除课程（只涉及 课程表）
    @ApiOperation(value = "删除课程（初步、单表）")
    @DeleteMapping("deleteCourseById/{courseId}")
    public R deleteCourseById(@PathVariable String courseId){
        eduCourseService.removeById(courseId);
        return R.ok();
    }


    //删除课程进阶（多张表）（同时删除该课程下的小节、章节、视频、描述）
    //TODO: 后面再完善视频的删除（01.26完成）
    @ApiOperation(value = "删除课程（进阶、多表）")
    @DeleteMapping("deleteCourseById2/{courseId}")
    public R deleteCourseById2(@PathVariable String courseId){
        //1、先删除小节视频
            //1-1 获取课程下面的视频id（根据课程id查询小节中的视频），封装成一个list
        List<String> eduVideosString = eduVideoService.getByCourseId(courseId);

        //如果不为空，删除阿里云视频
        if(!eduVideosString.isEmpty()){
            //1-3 将list 传入
            R result = vodClientInterface.deleteAliyunVideoBatch(eduVideosString);
            if(result.getCode() == 20001){  //失败
                return R.error().message("删除多个视频失败，触发熔断器。。。");
            }
        }




        //2、再删除课程
        eduCourseService.removeCourse(courseId);
        return R.ok();
    }


    ////////////////////////////////////////

    //生成订单


    //根据课程id查询课程信息
    //注意：直接返回对象，不要返回R，（因为返回R待会在其他后端接口取值不方便！）
    @ApiOperation(value = "根据课程id查询课程信息（生成订单）")
    @GetMapping("getOrderCourseById/{courseId}")
    public CourseOrder getOrderCourseById(@PathVariable String courseId){
        CourseOrder courseOrder = eduCourseService.getOrderCourseById(courseId);
        return courseOrder;//返回课程信息（含课程简介）
    }
}
