package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-23 01:45:17
 */
@ApiModel(value = "章节信息")
@Data
public class ChapterVo {
    //章节id
    @ApiModelProperty(value = "章节id")
    private String id;

    //章节标题
    @ApiModelProperty(value = "章节标题")
    private String title;



    //封装章节下面的小节
    @ApiModelProperty(value = "章节 --> 小节")
    private List<VideoVo> children = new ArrayList<>();
}
