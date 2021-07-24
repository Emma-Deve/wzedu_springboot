package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author wuze
 * @desc ...
 * @date 2021-02-01 13:05:13
 */
@Data
public class CourseFrontBaseInfoVo {

    @ApiModelProperty(value = "课程id")
    private String id;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程价格 0为免费")
    private BigDecimal price;

    @ApiModelProperty(value = "课程总课时")
    private Integer lessonNum;

    @ApiModelProperty(value = "课程封面")
    private String cover;

    @ApiModelProperty(value = "销售数量")
    private Long buyCount;

    @ApiModelProperty(value = "浏览数量")
    private Long viewCount;

    @ApiModelProperty(value = "课程简介")
    private String description;

    @ApiModelProperty(value = "讲师ID")
    private String teacherId;

    @ApiModelProperty(value = "讲师姓名")
    private String teacherName;

    @ApiModelProperty(value = "讲师资历（简介），一句话说明讲师")
    private String intro;

    @ApiModelProperty(value = "讲师头像")
    private String avatar;

    @ApiModelProperty(value = "一级课程类别ID")
    private String subjectLevelOneId;

    @ApiModelProperty(value = "一级课程类别名称")
    private String subjectLevelOne;

    @ApiModelProperty(value = "二级课程类别ID")
    private String subjectLevelTwoId;

    @ApiModelProperty(value = "二级课程类别名称")
    private String subjectLevelTwo;
}
