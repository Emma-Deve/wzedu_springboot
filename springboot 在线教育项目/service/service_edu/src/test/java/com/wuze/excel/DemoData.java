package com.wuze.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 22:48:23
 */
@Data
public class DemoData {

    //设置Excel表头名称
    @ExcelProperty(value = "学生编号",index = 0)
    private Integer sno;

    @ExcelProperty(value = "学生姓名",index = 1)
    private String sname;
}
