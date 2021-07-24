package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-21 22:05:33
 */
@ApiModel(value="课程基础信息", description="编辑课程基本信息的表单对象")
@Data
public class CourseInfoVo {

    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "二级分类ID")
    private String subjectId;

    //2021/01/22 16:57 添加
    @ApiModelProperty(value = "一级分类ID")
    private String subjectParentId;

    @ApiModelProperty(value = "课程标题")
    private String title;

    // 价格0.01 更精确
    @ApiModelProperty(value = "课程销售价格，设置为0则可免费观看")
    private BigDecimal price;

    @ApiModelProperty(value = "总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面图片路径")
    private String cover;

    @ApiModelProperty(value = "课程简介")
    private String description;

}
