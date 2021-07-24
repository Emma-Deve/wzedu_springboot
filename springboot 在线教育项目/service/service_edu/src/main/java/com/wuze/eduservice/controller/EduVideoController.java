package com.wuze.eduservice.controller;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.wuze.commonutils.R;
import com.wuze.eduservice.client.VodClientInterface;
import com.wuze.eduservice.entity.EduVideo;
import com.wuze.eduservice.service.EduVideoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin //跨域
public class EduVideoController {

    @Autowired
    VodClientInterface vodClientInterface;

    @Autowired
    EduVideoService eduVideoService;

    //添加小节
    //@RequestBody注解（json数据）
    @ApiOperation(value = "添加小节")
    @PostMapping("addVideo")
    public R addVideo(@RequestBody  EduVideo eduVideo){
        eduVideoService.save(eduVideo);
        return R.ok();
    }

    //修改小节
    //@RequestBody注解（json数据）
    //坑：调用方法updateById，不是save（难怪测试时总是有字段报错）
    @ApiOperation(value = "修改小节")
    @PostMapping("updateVideo")
    public R updateVideo(@RequestBody EduVideo eduVideo){
        eduVideoService.updateById(eduVideo);
        return R.ok();
    }

    //根据id查询小节
    @ApiOperation(value = "根据id查询小节")
    @GetMapping("getVideoById/{videoId}")
    public R getVideoById(@PathVariable String videoId){
        EduVideo video = eduVideoService.getById(videoId);
        return R.ok().data("video",video);
    }

    //删除小节（根据id）
    //TODO 小节里面还需要删除视频，后面完善（01/26 已完成）
    @ApiOperation(value = "删除小节")
    @DeleteMapping("deleteVideo/{videoId}")
    public R deleteVideo(@PathVariable String videoId){
        //1、先删除视频（小节id）
        EduVideo eduVideo = eduVideoService.getById(videoId);
        String videoSourceId = eduVideo.getVideoSourceId();//根据小节id找到里面的视频

        //判断 videoSourceId 是否为空
        if(StringUtils.isNotEmpty(videoSourceId)){  //不为空
            R result = vodClientInterface.deleteAliyunVideo(videoSourceId);
            if(result.getCode() == 20001){  //失败
                return R.error().message("删除单个视频失败，触发熔断器。。。");
            }
        }

        //2、再删除小节
        eduVideoService.removeById(videoId);
        return R.ok();
    }




}
