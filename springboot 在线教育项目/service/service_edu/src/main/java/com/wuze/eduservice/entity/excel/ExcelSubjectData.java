package com.wuze.eduservice.entity.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-21 01:46:33
 */
@Data
public class ExcelSubjectData {
    //一级分类
    //设置excel表头名称
    @ExcelProperty(value = "一级分类",index=0)
    private String oneSubjectName;

    @ExcelProperty(value = "二级分类",index=1)
    private String twoSubjectName;

}
