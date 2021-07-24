package com.wuze.eduservice.controller;


import com.wuze.commonutils.R;
import com.wuze.eduservice.entity.EduChapter;
import com.wuze.eduservice.entity.vo.ChapterVo;
import com.wuze.eduservice.service.EduChapterService;
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
@RequestMapping("/eduservice/edu-chapter")
@CrossOrigin //跨域
public class EduChapterController {

    @Autowired
    EduChapterService eduChapterService;

    //获取各章节的小节
    @ApiOperation(value = "获取各章节的小节")
    @GetMapping("getChapterVideo/{courseId}")
    public R getChapterVideo(@PathVariable String courseId){
        //注：返回的List实体是ChapterVo
        List<ChapterVo> allChapterVideo= eduChapterService.getChapterVideo(courseId);
        return R.ok().data("allChapterVideo",allChapterVideo);
    }

    //添加章节
    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        //实体类EduChapter
        eduChapterService.save(eduChapter);
        return R.ok();
    }

    //根据id查询章节
    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        //注：返回的List实体是ChapterVo
        EduChapter eduChapter = eduChapterService.getById(id);
        //返回查询出来的章节
        return R.ok().data("eduChapter",eduChapter);
    }

    //修改章节
    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        //实体类EduChapter
        eduChapterService.updateById(eduChapter);
        return R.ok();
    }

    //删除章节（根据id）
    @ApiOperation(value = "修改章节")
    @DeleteMapping("deleteChapter/{id}")
    public R deleteChapter(@PathVariable String id){
        //实体类EduChapter
        eduChapterService.deleteChapterById(id);
        return R.ok();
    }
}

