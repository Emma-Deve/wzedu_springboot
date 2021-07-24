package com.wuze.eduservice.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-23 01:47:32
 */
@ApiModel(value = "课时信息")
@Data
public class VideoVo {

    //小节id
    @ApiModelProperty(value = "小节id")
    private String id;

    //小节标题
    @ApiModelProperty(value = "小节标题")
    private String title;

    //小节id
    @ApiModelProperty(value = "章节id")
    private String chapterId;

    //视频id
    //0201补充
    //注意：这里的字段名必须为videoSourceId，不能为 videoId或其他，
    // 因为这个实体后面使用BeanUtils.copyProperties封装，只能识别相同名称属性
    @ApiModelProperty(value = "视频id")
    private String videoSourceId;

}
