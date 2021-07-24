package com.wuze.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.eduservice.entity.EduSubject;
import com.wuze.eduservice.entity.excel.ExcelSubjectData;
import com.wuze.eduservice.entity.subject.Onesubject;
import com.wuze.eduservice.entity.subject.TwoSubject;
import com.wuze.eduservice.listener.SubjectExcelListener;
import com.wuze.eduservice.mapper.EduSubjectMapper;
import com.wuze.eduservice.service.EduSubjectService;
import com.wuze.servicebase.exceptionhandler.WzException;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-20
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {

    @Override
    public void importSubjectData(MultipartFile file, EduSubjectService eduSubjectService) {

        try {
            //获取文件输入流(需要 try-catch)
            InputStream inputStream = file.getInputStream();

            //参数1：文件输入流
            //参数2：Excel相关实体类
            //参数3：监听器
            EasyExcel.read(inputStream, ExcelSubjectData.class,new SubjectExcelListener(eduSubjectService)).sheet().doRead();

        }catch (WzException | IOException e){
            e.printStackTrace();
            throw new WzException(20002,"添加课程分类失败");
        }



    }

    //课程分类列表（树形）
    @Override
    public List<Onesubject> getAllOneTwoSubject() {
        //1、查询所有一级分类（parent_id = 0）
        //查询结果封装到EduSubject（因为EduSubject是链接到数据库的实体类）
        //返回结果封装成list
        QueryWrapper<EduSubject> wrapperOne = new QueryWrapper<>();
        wrapperOne.eq("parent_id","0");
        List<EduSubject> oneSubjectList = baseMapper.selectList(wrapperOne);

        //2、查询所有二级分类（parent_id != 0）
        //查询结果封装到EduSubject（因为EduSubject是链接到数据库的实体类）
        //返回结果封装成list
        QueryWrapper<EduSubject> wrapperTwo = new QueryWrapper<>();
        wrapperTwo.ne("parent_id","0");
        List<EduSubject> twoSubjectList = baseMapper.selectList(wrapperTwo);

        //////////////////////////

        //3、转换封装格式
        //创建list集合，用于存储最终封装格式数据
        //思路：将EduSubject查询出来的集合，遍历之后赋值给Onesubject
        //具体思路(方法一)：遍历，取出EduSubject类对象里面的id和title，然后再set进Onesubject类对象，然后再将对象存入List对象finalSubjectList中
        //（更好！）方法二：利用BeanUtils工具类的 copyProperties 方法
        List<Onesubject> finalSubjectList = new ArrayList<>();


        //3-1、封装一级分类
        for (int i = 0; i < oneSubjectList.size(); i++) {
            EduSubject eduSubject = oneSubjectList.get(i);//获取每一个对象，赋值
            Onesubject onesubject = new Onesubject();

            //将每一个eduSubject对象复制给onesubject对象
            //注：onesubject对象只有id和title，但是eduSubject有很多属性，只找对应的属性做封装，其他忽略
            BeanUtils.copyProperties(eduSubject,onesubject);

            finalSubjectList.add(onesubject);//将每个onesubject添加进List列表



            //3-2、封装二级分类（嵌套）
            //先创建list集合封装每一个一级分类的二级分类
            List<TwoSubject> twoFinalSubjectList = new ArrayList<>();

            for (int j = 0; j < twoSubjectList.size(); j++) {
                EduSubject eduSubjecttwo = twoSubjectList.get(j);//获取每一个对象，赋值
                TwoSubject twoSubject = new TwoSubject();

                //判断二级分类parent_id 和 一级分类id 是否一样
                //具体思路：如果一样，把eduSubjecttwo 复制到twoSubject 里面，然后放到twoFinalSubjectList里面
                if(eduSubjecttwo.getParentId().equals(eduSubject.getId())){
                    //将每一个eduSubject对象复制给twoSubject对象
                    BeanUtils.copyProperties(eduSubjecttwo,twoSubject);
                    twoFinalSubjectList.add(twoSubject);

                }
            }



            //最后一步，把二级分类放到一级分类里面

            /*因为一级分类的实体类有一个List，专门存放二级分类，如下代码：

                //一个一级分类有多个二级分类（List封装）
                private List<TwoSubject> children = new ArrayList<>();
            * */
            onesubject.setChildren(twoFinalSubjectList);

        }

        return finalSubjectList;
    }


    //01/23 补充：根据课程分类id查找课程(返回课程标题)（一级二级均可）
    @Override
    public String getSubjectById(String subjectId) {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("id",subjectId);
        EduSubject eduSubjectById = baseMapper.selectOne(wrapper);
        String title = eduSubjectById.getTitle();
        return title;
    }
}













