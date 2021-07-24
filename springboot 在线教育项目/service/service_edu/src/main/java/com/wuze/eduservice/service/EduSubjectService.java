package com.wuze.eduservice.service;

import com.wuze.eduservice.entity.EduSubject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.eduservice.entity.subject.Onesubject;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-20
 */
public interface EduSubjectService extends IService<EduSubject> {
    void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService);

    //获取所有课程分类信息（一级+二级）
    List<Onesubject> getAllOneTwoSubject();

    //01/23 补充：根据课程分类id查找课程(返回课程标题)（一级二级均可）
    String getSubjectById(String subjectId);
}
