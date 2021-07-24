package com.wuze.eduservice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.eduservice.entity.EduVideo;

import java.util.List;

/**
 * <p>
 * 课程视频 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
public interface EduVideoService extends IService<EduVideo> {

    void removeVideoByCourseId(String courseId);

    List<String> getByCourseId(String courseId);
}
