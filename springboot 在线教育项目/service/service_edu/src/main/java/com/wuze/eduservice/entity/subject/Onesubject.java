package com.wuze.eduservice.entity.subject;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-21 16:55:23
 */
//一级分类
@Data
public class Onesubject {
    private String id;
    private String title;

    //一个一级分类有多个二级分类（List封装）
    private List<TwoSubject> children = new ArrayList<>();
}
