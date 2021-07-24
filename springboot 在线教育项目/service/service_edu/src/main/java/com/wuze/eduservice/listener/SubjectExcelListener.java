package com.wuze.eduservice.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wuze.eduservice.entity.EduSubject;
import com.wuze.eduservice.entity.excel.ExcelSubjectData;
import com.wuze.eduservice.service.EduSubjectService;
import com.wuze.servicebase.exceptionhandler.WzException;

import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-21 01:57:57
 */
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {

    ////////////////////////
    ////////////////////////
    public EduSubjectService eduSubjectService;

    //无参构造
    public SubjectExcelListener() {
    }
    //有参构造（传递eduSubjectService用于操作数据库）
    public SubjectExcelListener(EduSubjectService eduSubjectService) {
        this.eduSubjectService = eduSubjectService;
    }
    ////////////////////////
    ////////////////////////



    //一行一行读取 Excel 内容
    @Override
    public void invoke(ExcelSubjectData excelData, AnalysisContext analysisContext) {

        //excel为空
        if(excelData == null){
            throw new WzException(20001,"添加失败（excel为空）");
        }

        //添加一级分类（先判断是否重复）
        //excelSubjectData.getOneSubjectName() 获取名称（一级标题）
        EduSubject existOneSubject = this.exitOneSubject(eduSubjectService,excelData.getOneSubjectName());
        if(existOneSubject == null){    //没有相同的（即这个标题还未被添加）
            //创建对象并保存这个分类的信息
            existOneSubject = new EduSubject();
            existOneSubject.setTitle(excelData.getOneSubjectName());//获取一级标题
            existOneSubject.setParentId("0");
            eduSubjectService.save(existOneSubject);//保存进数据库
        }



        //添加二级分类
        //（1）获取一级分类id值
        String pid=existOneSubject.getId();

        //（2）实现添加功能(先判断是否重复)
        EduSubject existTwoSubject = this.exitTwoSubject(eduSubjectService,excelData.getTwoSubjectName(),pid);
        if(existTwoSubject ==null ){    //没有相同（未被添加）
           //创建对象并保存该分类信息
            existTwoSubject=new EduSubject();
            existTwoSubject.setTitle(excelData.getTwoSubjectName());//获取二级标题
            existTwoSubject.setParentId(pid);//设置父id
            eduSubjectService.save(existTwoSubject);//保存进数据库
        }

    }



    //判断一级分类是否重复
    //返回EduSubject实体类对象
    public EduSubject exitOneSubject(EduSubjectService eduSubjectService,String name){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();

        wrapper.eq("title",name);//查询title字段等于name的一条数据
        wrapper.eq("parent_id","0");//查询parent_id字段等于0的一条数据
        //wrapper现在的条件是title=name,parent_id=0的数据

        //利用wrapper查询一条数据（对象）
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);

        //将对象返回
        return eduSubject;
    }


    //判断二级分类是否重复
    //返回EduSubject实体类对象
    public EduSubject exitTwoSubject(EduSubjectService eduSubjectService,String name,String pid){
        QueryWrapper<EduSubject> wrapper=new QueryWrapper<>();
        wrapper.eq("title",name);//查询title字段等于name的一条数据
        wrapper.eq("parent_id",pid);//查询parent_id字段等于pid的一条数据
        //利用wrapper查询一条数据（对象）
        EduSubject eduSubject = eduSubjectService.getOne(wrapper);
        return eduSubject;
    }






    //读取表头内容
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        System.out.println("表头信息："+headMap);
    }

    //读取完成之后执行
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("读取完成");
    }
}
