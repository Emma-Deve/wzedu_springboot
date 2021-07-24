package com.wuze.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.eduservice.entity.EduChapter;
import com.wuze.eduservice.entity.EduVideo;
import com.wuze.eduservice.entity.vo.ChapterVo;
import com.wuze.eduservice.entity.vo.VideoVo;
import com.wuze.eduservice.mapper.EduChapterMapper;
import com.wuze.eduservice.service.EduChapterService;
import com.wuze.eduservice.service.EduVideoService;
import com.wuze.servicebase.exceptionhandler.WzException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {

    @Autowired
    private EduVideoService eduVideoService;

    //获取各章节的小节
    @Override
    public List<ChapterVo> getChapterVideo(String courseId) {
        //1、获取各个章节 ( Chapter )
        QueryWrapper<EduChapter> wrapperChapter = new QueryWrapper<>();
        //注意（坑）：这里查询的是数据库字段，所以要写course_id，不能写courseId
        wrapperChapter.eq("course_id",courseId);
        List<EduChapter> eduChapterList = baseMapper.selectList(wrapperChapter);    // .selectList()



        //2、获取各个小节 ( Video )
        //因为Video 是另一张表的，没办法用 baseMapper，只能用 @Autowired 注入
        QueryWrapper<EduVideo> wrapperVideo = new QueryWrapper<>();
        //注意（坑）：这里查询的是数据库字段，所以要写course_id，不能写courseId
        wrapperVideo.eq("course_id",courseId);
        List<EduVideo> eduVideoList = eduVideoService.list(wrapperVideo);   // .list()



        //3、转换整合封装（章节对应小节）

        //3-1 最后返回的章节List（其children属性封装各个小节信息）
        List<ChapterVo> finalChapterVideoList = new ArrayList<>();

        //3-2 遍历所有章节
        for (int i = 0; i < eduChapterList.size(); i++) {
            //3-2-1 添加章节
            EduChapter eduChapter = eduChapterList.get(i);//获取每个章节
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(eduChapter,chapterVo);//将每个章节对象 复制 给 章节Vo 对象
            finalChapterVideoList.add(chapterVo);//将每个章节添加到List

            //3-2-2 添加每个章节的小节
            List<VideoVo> videoList = new ArrayList<>();
            // 遍历某一章节中的所有小节
            for (int j = 0; j < eduVideoList.size(); j++) {
                //判断该小节是否对应该章节，如果对应，添加
                //注意（坑）：这里是get(j) 不是 i，难怪查不出小节信息（排错了很久...）
                EduVideo eduVideo = eduVideoList.get(j);
                //注意（坑）：因为id是String类型，所以这里不能用 == 比较，要用 equals，否则小节信息查不出来
                if(eduVideo.getChapterId().equals(eduChapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(eduVideo,videoVo);//eduVideo -> videoVo
                    videoList.add(videoVo);

                }
            }
            chapterVo.setChildren(videoList);//将对应的小节加到对应的章节（匹配成功）
        }
        return finalChapterVideoList;

    }

    //根据id删除章节（判断是否有小节、是否允许删除）
    @Override
    public boolean deleteChapterById(String id) {
        //如果查询到这个小节有章节id对应传入的id（也就是这个章节有小节），就不允许删除
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("chapter_id",id);
        int count = eduVideoService.count(wrapper);
        if(count>0){    //count>0 有小节
            throw new WzException(20001,"该章节下面还有小节，不允许删除");
        }
        else{   //否则，删除该章节
            int flag = baseMapper.deleteById(id);
            //如果flag>0，删除成功 1>0 true
            //0>0  删除失败 false
            return flag>0;
        }

    }

    //根据课程id删除章节
    @Override
    public void removeChapterByCourseId(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }
}
