package com.wuze.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.commonutils.OrderCommonClassVo.CourseOrder;
import com.wuze.eduservice.entity.EduCourse;
import com.wuze.eduservice.entity.EduCourseDescription;
import com.wuze.eduservice.entity.vo.*;
import com.wuze.eduservice.mapper.EduCourseMapper;
import com.wuze.eduservice.service.EduChapterService;
import com.wuze.eduservice.service.EduCourseDescriptionService;
import com.wuze.eduservice.service.EduCourseService;
import com.wuze.eduservice.service.EduVideoService;
import com.wuze.servicebase.exceptionhandler.WzException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
@Service
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {

    //注：因为需要操作到 “课程描述表”，但是这个接口又不是 这个表的，所以利用自动注入，反正都在spring容器中
    @Autowired
    private EduCourseDescriptionService eduCourseDescriptionService;

    @Autowired
    private EduVideoService eduVideoService;

    @Autowired
    private EduChapterService eduChapterService;

    //把service注入
    @Autowired
    private EduCourseService eduCourseService;


    //添加课程信息（同时操作两张表）
    @Override
    public String saveCourseInfo(CourseInfoVo courseInfoVo) {
        //1、向课程表添加信息
        //edu_course （因为这里是这张表的ServiceImpl，所以可以直接使用baseMapper）
        //注意：Vo 需要 转换 成 对应数据库表
        EduCourse eduCourse=new EduCourse();
        BeanUtils.copyProperties(courseInfoVo,eduCourse);// 参数1 复制给 参数2

        //（自己挖的坑：01/22 19:53  ==》 前端添加课程界面，添加进来的都是一级分类为0，很奇怪，原来是自己设置了默认值！）
        //eduCourse.setSubjectParentId("0");//报错：先补充，设置默认值0（ 01/21 23:23）

        int flag = baseMapper.insert(eduCourse);//返回成功或失败 boolean
        if(flag==0){
            throw new WzException(20001,"添加课程信息失败");
        }

        //注意：因为两个表是 1:1 关系，是通过 主键id 梦幻联动的，所以id要相同
        //（1）获取课程id
        String cid=eduCourse.getId();

        //2、向课程描述表添加信息
        //edu_course_description
        //直接用 @Autowired 注入 eduCourseDescriptionService（Spring容器）
        EduCourseDescription CourseDescription=new EduCourseDescription();
        CourseDescription.setDescription(courseInfoVo.getDescription());
        //（2）手动设置id给课程描述表
        CourseDescription.setId(cid);
        eduCourseDescriptionService.save(CourseDescription);//传进去的必须是实体类（实体类又是对应数据库表的）


        //返回课程id
        return cid;
    }



    //根据课程id查询课程信息
    //封装给 CourseInfoVo
    @Override
    public CourseInfoVo getCourseById(String courseId) {

        //查询课程基本信息（course表）
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseInfoVo courseInfoVo = new CourseInfoVo();
        BeanUtils.copyProperties(eduCourse,courseInfoVo);

        //查询课程简介（course_description表）
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseInfoVo.setDescription(eduCourseDescription.getDescription());//一个属性而已，直接set，当然也可以用 BeanUtils,但没必要

        return courseInfoVo;
    }

    //修改课程信息
    @Override
    public void updateCourseInfo(CourseInfoVo courseInfoVo) {
        //1、修改课程基本信息（course 表）
        EduCourse eduCourse = new EduCourse();
        //eduCourse.setId(courseInfoVo.getId());
        BeanUtils.copyProperties(courseInfoVo,eduCourse);
        int flag = baseMapper.updateById(eduCourse);
        if(flag==0){
            //修改失败
            throw new WzException(20001,"修改课程信息失败");
        }


        //2、修改课程简介（course_description表）
        EduCourseDescription eduCourseDescription = new EduCourseDescription();
        eduCourseDescription.setId(courseInfoVo.getId());
        eduCourseDescription.setDescription(courseInfoVo.getDescription());
        eduCourseDescriptionService.updateById(eduCourseDescription);
    }


    //课程发布页面（获取所有相关信息显示）
    @Override
    public CoursePublishVo getPublishCourseInfo(String courseId) {
        //用 baseMapper 调用刚刚在Mapper写的方法（传入 courseId 作为参数）
         CoursePublishVo coursePublishVo = baseMapper.getPublishCourseInfo(courseId);
        return coursePublishVo;
    }

    //////////////////////////////////

    //课程列表（模糊查询 + 分页）
    @Override
    public void pageQuery(Page<EduCourse> pageCourse, CourseQuery courseQuery) {
            //添加wrapper，构造条件
            QueryWrapper<EduCourse> wrapper=new QueryWrapper<>();
            //多条件组合查询（模糊查询）（这些条件可能 有些有 有些没有 --> 动态sql）
            String name=courseQuery.getName();
            String status = courseQuery.getStatus();
            //即判断条件值是否为空，如果不为空则拼接条件
            //注意（坑）：课程表的 课程名称 字段 是 “title”，不是name
            if(!StringUtils.isEmpty(name)) {//如果不为空 //StringUtils导入的是baomidou的
                wrapper.like("title",name);//第一个参数是数据库 表的列名（字段名）
            }
            //同理


            //状态
            if (!StringUtils.isEmpty(status)) {//如果不为空 //StringUtils导入的是baomidou的
                wrapper.eq("status", status);//第一个参数是数据库 表的列名（字段名）
            }

            eduCourseService.page(pageCourse,wrapper);

    }


    //删除课程进阶（多张表）（同时删除该课程下的小节、章节、视频、描述）
    //需要注入另外几个表（几个实体类）
    //TODO: 后面再完善视频的删除
    @Override
    public void removeCourse(String courseId) {
        //1、删除小节（注：根据课程id，不是小节id）
        eduVideoService.removeVideoByCourseId(courseId);

        //2、删除章节（注：根据课程id，不是小节id）
        eduChapterService.removeChapterByCourseId(courseId);

        //3、删除课程简介（注：根据课程id，不是小节id）
        eduCourseDescriptionService.removeDescriptionByCourseId(courseId);

        //4、删除课程本身
        int flag = baseMapper.deleteById(courseId);

        //删除失败
        if(flag==0){
            throw new WzException(20001,"删除课程失败");
        }

    }

    //前台系统，条件带分页查询课程列表
    @Override
    public Map<String, Object> getCourseVoPageMap(Page<EduCourse> page, CourseFrontVo courseFrontVo) {

        QueryWrapper<EduCourse> wrapper = new QueryWrapper<>();

        //查询一级分类
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectParentId())) {
            wrapper.eq("subject_parent_id", courseFrontVo.getSubjectParentId());
        }

        //查询二级分类
        if(!StringUtils.isEmpty(courseFrontVo.getSubjectId())) {
            wrapper.eq("subject_id", courseFrontVo.getSubjectId());
        }

        //排序（销量、时间、价格）
        //这几个字段可能为空，所以要先进行判断
        //1、销量降序
        if(!StringUtils.isEmpty(courseFrontVo.getBuyCountSort())){
            //注意：名称对应数据库字段（列名），不是实体类名称！
            wrapper.orderByDesc("buy_count");
        }

        //2、时间降序
        if(!StringUtils.isEmpty(courseFrontVo.getGmtCreateSort())){
            //注意：名称对应数据库字段（列名），不是实体类名称！
            wrapper.orderByDesc("gmt_create");
        }

        //3、价格降序
        if(!StringUtils.isEmpty(courseFrontVo.getPriceSort())){
            //注意：名称对应数据库字段（列名），不是实体类名称！
            wrapper.orderByDesc("price");
        }

        baseMapper.selectPage(page, wrapper);

        //将查询出来的列表放到map集合中
        HashMap<String, Object> map = new HashMap();
        //用 page对象 get
        List<EduCourse> records = page.getRecords();
        long current = page.getCurrent();//当前页
        long pages = page.getPages();//总页数
        long total = page.getTotal();
        long size = page.getSize();
        boolean hasPrevious = page.hasPrevious();//上一页
        boolean hasNext = page.hasNext();//下一页

        map.put("records",records);
        map.put("current",current);
        map.put("pages",pages);
        map.put("total",total);
        map.put("size",size);
        map.put("hasPrevious",hasPrevious);
        map.put("hasNext",hasNext);

        return map;
    }



    //课程基本信息(路径传入课程id)
    //多表查询
    @Override
    public CourseFrontBaseInfoVo getCourseBaseInfo(String courseId) {
        //调用Mapper 方法（mapper那边绑定了xml的sql语句）
        return baseMapper.getCourseBaseInfoMapper(courseId);
    }





    ///////////////////////////////////
    //生成订单


    //根据课程id获取课程信息（生成订单）
    @Override
    public CourseOrder getOrderCourseById(String courseId) {
        //查询课程基本信息（course表）
        EduCourse eduCourse = baseMapper.selectById(courseId);
        CourseOrder courseOrder = new CourseOrder();
        BeanUtils.copyProperties(eduCourse,courseOrder);

        //查询课程简介（course_description表）
        EduCourseDescription eduCourseDescription = eduCourseDescriptionService.getById(courseId);
        courseOrder.setDescription(eduCourseDescription.getDescription());//一个属性而已，直接set，当然也可以用 BeanUtils,但没必要

        return courseOrder;
    }
}
