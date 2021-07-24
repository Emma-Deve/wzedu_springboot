package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-24 20:23:26
 */
@Data
public class CourseQuery {

    @ApiModelProperty(value = "课程名称,模糊查询")
    private String name;

    @ApiModelProperty(value = "发布状态 Normal:已发布 Draft:未发布")
    private String status;


}
