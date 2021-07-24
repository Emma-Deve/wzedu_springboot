package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-24 15:57:12
 */
@Data
public class CoursePublishVo {
    @ApiModelProperty(value = "课程ID")
    private String id;

    @ApiModelProperty(value = "课程标题")
    private String title;

    @ApiModelProperty(value = "课程封面")
    private String cover;

    @ApiModelProperty(value = "课程价格")
    private String price;

    @ApiModelProperty(value = "课时数")
    private String lessonNum;

    @ApiModelProperty(value = "讲师名称")
    private String teacherName;

    @ApiModelProperty(value = "一级分类")
    private String subjectLevelOne;

    @ApiModelProperty(value = "二级分类")
    private String subjectLevelTwo;
}
