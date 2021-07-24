package com.wuze.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.eduservice.entity.EduTeacher;
import com.wuze.eduservice.entity.vo.TeacherQuery;
import com.wuze.eduservice.mapper.EduTeacherMapper;
import com.wuze.eduservice.service.EduTeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-03
 */
@Service
public class EduTeacherServiceImpl extends ServiceImpl<EduTeacherMapper, EduTeacher> implements EduTeacherService {

    //把service注入
    @Autowired
    private EduTeacherService eduTeacherService;

    @Override
    public void pageQuery(Page<EduTeacher> pageTeacher, TeacherQuery teacherQuery) {
        //添加wrapper，构造条件
        QueryWrapper<EduTeacher> wrapper=new QueryWrapper<>();
        //多条件组合查询（模糊查询）（这些条件可能 有些有 有些没有 --> 动态sql）
        String name=teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        //即判断条件值是否为空，如果不为空则拼接条件
        if(!StringUtils.isEmpty(name)) {//如果不为空 //StringUtils导入的是baomidou的
            wrapper.like("name",name);//第一个参数是数据库 表的列名（字段名）
        }
        //同理

        //level虽然是Integer型，但可以用StringUtils.isEmpty(level)这样判断
        //import org.springframework.util.StringUtils;
        if (!StringUtils.isEmpty(level)) {//如果不为空 //StringUtils导入的是baomidou的
            wrapper.eq("level", level);//第一个参数是数据库 表的列名（字段名）
        }



        if(!StringUtils.isEmpty(begin)) {//如果不为空 //StringUtils导入的是baomidou的
            //ge:大于等于
            wrapper.ge("gmt_create",begin);//第一个参数是数据库 表的列名（字段名）gmt_create；（不是entity下的属性名！）
        }

        if(!StringUtils.isEmpty(end)) {//如果不为空 //StringUtils导入的是baomidou的
            //le:小于等于
            wrapper.le("gmt_create",end);//第一个参数是数据库 表的列名（字段名）
        }

        //根据创建时间排序（2021.01.19）
        wrapper.orderByDesc("gmt_create");

        eduTeacherService.page(pageTeacher,wrapper);
    }



    //分页查询名师列表
    @Override
    public Map<String, Object> selectTeacherPage(Page<EduTeacher> teacherPage) {

        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("id");

        eduTeacherService.page(teacherPage,wrapper);
        List<EduTeacher> records = teacherPage.getRecords();
        long current = teacherPage.getCurrent();
        long size = teacherPage.getSize();
        long total = teacherPage.getTotal();
        long pages = teacherPage.getPages();//补充
        boolean hasNext = teacherPage.hasNext();//下一页
        boolean hasPrevious = teacherPage.hasPrevious();//上一页

        HashMap<String, Object> map = new HashMap();
        map.put("records",records);
        map.put("current",current);
        map.put("size",size);
        map.put("total",total);
        map.put("pages",pages);//补充
        map.put("hasNext",hasNext);//传入的key名称，前端要取的，所以key命名要规范
        map.put("hasPrevious",hasPrevious);//传入的key名称，前端要取的，所以key命名要规范

        return map;
    }
}
