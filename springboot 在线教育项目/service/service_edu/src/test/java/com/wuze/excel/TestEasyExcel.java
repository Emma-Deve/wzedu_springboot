package com.wuze.excel;

import com.alibaba.excel.EasyExcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 22:50:33
 */
public class TestEasyExcel {
    public static void main(String[] args) {
        //实现Excel写操作
        //1、设置写入文件夹地址和excel文件名称（反斜杠）
        //String filename="C:\\Users\\w1396\\Desktop\\testeasyexcel.xlsx";

        //2、调用easyexcel里面的方法实现写操作
        //write方法两个参数：第一个参数是文件路径名称，第二个参数是实体类class
        //EasyExcel.write(filename,DemoData.class).sheet("学生列表").doWrite(getData());




        //实现Excel读操作
        String filename="C:\\Users\\w1396\\Desktop\\testeasyexcel.xlsx";
        EasyExcel.read(filename,DemoData.class,new ExcelListener()).sheet().doRead();

    }

    private static List<DemoData> getData(){
        List<DemoData> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemoData data=new DemoData();
            data.setSno(i);
            data.setSname("wz"+i);
            list.add(data);
        }
        return list;
    }
}
