package com.wuze.eduservice.controller.front;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wuze.commonutils.R;
import com.wuze.eduservice.client.OrderClientInterface;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.vo.ChapterVo;
import com.wuze.eduservice.entity.vo.CourseFrontBaseInfoVo;
import com.wuze.eduservice.entity.vo.CourseFrontVo;
import com.wuze.eduservice.service.EduChapterService;
import com.wuze.eduservice.service.EduCourseService;
import com.wuze.eduservice.utils.JwtUtils;
import com.wuze.servicebase.exceptionhandler.WzException;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-31 20:40:56
 */
@RestController
@RequestMapping("/eduservice/course")
@CrossOrigin
public class CourseFrontController {
    @Autowired
    private EduCourseService courseService;

    @Autowired
    private EduChapterService chapterService;

    @Autowired
    private OrderClientInterface orderClientInterface;//远程调用

    //条件带分页查询课程列表
    //required = false 表示这个参数可以为空
    //注意：@RequestBody要对应post提交！！！
    @ApiOperation(value = "条件带分页查询课程列表")
    @PostMapping("getCourseVoPage/{current}/{limit}")
    public R getCourseVoPage(@PathVariable long current, @PathVariable long limit,
                             @RequestBody(required = false) CourseFrontVo courseFrontVo){
        Page<EduCourse> page = new Page<>(current,limit);

        Map<String, Object> courseVoPageMap = courseService.getCourseVoPageMap(page,courseFrontVo);

        return R.ok().data(courseVoPageMap);
    }


    //课程基本信息(路径传入课程id)
    //多表查询
    @ApiOperation(value = "课程基本信息")
    @GetMapping("getCourseBaseInfoVo/{courseId}")
    public R getCourseBaseInfoVo(@PathVariable String courseId, HttpServletRequest request){

        //1、sql语句，根据课程id查询课程信息
        CourseFrontBaseInfoVo courseBaseInfoVo = courseService.getCourseBaseInfo(courseId);

        //2、根据课程id查询章节信息
        //调用 EduChapter 方法
        List<ChapterVo> chapterVideo = chapterService.getChapterVideo(courseId);

        //远程调用
        //购买课程后补充（02/06）
        //注：JwtUtils如果放在 common_utils 包，读取不到，所以新建一个JwtUtils类在edu模块
        if(JwtUtils.getMemberIdByJwtToken(request) == null){
            throw new WzException(20001,"您还未登录，请登录后再操作");
        }
        boolean isBuy = orderClientInterface.OrderIsBuy(courseId, JwtUtils.getMemberIdByJwtToken(request));

        return R.ok().data("courseBaseInfoVo",courseBaseInfoVo).data("chapterVideo",chapterVideo).data("isBuy",isBuy);
    }
}
