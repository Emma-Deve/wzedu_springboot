package com.wuze.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.eduservice.entity.EduChapter;
import com.wuze.eduservice.entity.vo.ChapterVo;

import java.util.List;

/**
 * <p>
 * 课程 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
public interface EduChapterService extends IService<EduChapter> {

    //获取各章节的小节
    List<ChapterVo> getChapterVideo(String courseId);

    //根据id删除章节（判断是否有小节、是否允许删除）
    boolean deleteChapterById(String id);

    void removeChapterByCourseId(String courseId);
}
