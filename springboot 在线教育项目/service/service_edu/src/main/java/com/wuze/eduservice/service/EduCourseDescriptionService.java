package com.wuze.eduservice.service;

import com.wuze.eduservice.entity.EduCourseDescription;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 课程简介 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
public interface EduCourseDescriptionService extends IService<EduCourseDescription> {

    void removeDescriptionByCourseId(String courseId);
}
