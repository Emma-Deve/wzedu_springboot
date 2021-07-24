package com.wuze.eduservice.controller;


import com.wuze.commonutils.R;
import com.wuze.eduservice.entity.subject.Onesubject;
import com.wuze.eduservice.service.EduSubjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-01-20
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@CrossOrigin //跨域
public class EduSubjectController {

    @Autowired
    EduSubjectService eduSubjectService;

    //统一返回结果 R
    //MultipartFile 获取上传的excel文件
    //Post提交
    @ApiOperation(value = "添加课程分类（easyexcel）")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        //调用实现方法
        //将eduSubjectService作为参数传入（因为后面监听类没有交给spring管理，无法使用eduSubjectService操作数据库）
        eduSubjectService.importSubjectData(file,eduSubjectService);

        return R.ok();//默认返回结果
    }


    //获取所有课程分类信息（一级+二级）
    //直接查一级，就能查出所有（因为一级绑定了二级）
    @ApiOperation(value = "获取所有课程分类信息（一级+二级）")
    @GetMapping("getAllSubject")
    public R getAllOneTwoSubject(){
        List<Onesubject> allSubject= eduSubjectService.getAllOneTwoSubject();
        //测试
        //System.out.println(allSubject.get(1));
        return R.ok().data("allSubjectList",allSubject);
    }


    //01/23 补充：根据课程分类id查找课程(返回课程标题)（一级二级均可）
    @ApiOperation(value = "根据课程分类id查找课程(返回课程标题)")
    @GetMapping("getSubjectById/{subjectId}")
    public R getSubjectById(@PathVariable String subjectId){
        String title = eduSubjectService.getSubjectById(subjectId);
        return R.ok().data("title",title);
    }

}

