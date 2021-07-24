package com.wuze.eduservice.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wuze.eduservice.entity.EduTeacher;
import com.wuze.eduservice.entity.vo.TeacherQuery;

import java.util.Map;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author wuze
 * @since 2021-01-03
 */
public interface EduTeacherService extends IService<EduTeacher> {
    void pageQuery(Page<EduTeacher>  pageTeacher, TeacherQuery teacherQuery);

    //分页查询名师列表（每页8条数据）
    Map<String, Object> selectTeacherPage(Page<EduTeacher> teacherPage);
}
