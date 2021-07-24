package com.wuze.vod.controller;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.wuze.commonutils.R;
import com.wuze.servicebase.exceptionhandler.WzException;
import com.wuze.vod.service.VodService;
import com.wuze.vod.utils.ConstantVodUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.wuze.vod.utils.InitVodClient.initVodClient;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-25 16:46:10
 */
@RestController
@RequestMapping("/eduvod/filevod")
@CrossOrigin    //跨域
public class VodController {

    @Autowired
    private VodService vodService;

    @PostMapping("getAliyunVodId")
    public R getAliyunVodId(MultipartFile file) throws IOException {
        String vodId = vodService.getAliyunVodId(file);
        return R.ok().data("vodId",vodId);
    }


    //根据视频id删除阿里云视频
    //参考官方文档
    //注意（坑）：这里的删除，是要删除刚刚上传至阿里云点播平台的视频（注意：这个当前还没保存进数据库的）
    @ApiOperation(value = "根据视频id删除阿里云视频")
    @DeleteMapping("deleteAliyunVideo/{videoSourceId}")
    public R deleteAliyunVideo(@PathVariable String videoSourceId){
        try {
            //1、初始化对象
            DefaultAcsClient client = initVodClient("LTAI4GAL4KRB17VWSeVv2s5g", "2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7");
            //2、创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();
            //3、向request设置视频id，支持传入多个视频ID，多个用逗号分隔
            request.setVideoIds(videoSourceId);
            //4、调用初始化对象的方法实现删除
            client.getAcsResponse(request);
            return R.ok();

        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new WzException(20001,"删除视频失败");
        }

    }

    //删除阿里云多个视频（根据List集合）
    //可以加参数 @RequestParam("videoList") 表示前端传进来的变量名称一定为videoList（限制前端传入的变量名）
    @ApiOperation(value = "批量删除阿里云视频")
    @DeleteMapping("deleteAliyunVideoBatch")
    public R deleteAliyunVideoBatch(@RequestParam(value = "videoList", required = false) List<String> videoList){
        vodService.removeAliyunVideoBatch(videoList);
        return R.ok();

    }




    //根据视频id获取播放凭证
    //课程预览功能
    //获取播放凭证 方法二 (思路更清晰)
    // 02.01
    @ApiOperation(value = "根据视频id获取播放凭证（课程视频预览）")
    @GetMapping("getAliyunPlayAuth/{videoId}")
    public R  getAliyunPlayAuth(@PathVariable String videoId) {
        try {
            //1、创建初始化对象
            //initVodClient 参数1：AccessKeyId、参数2：AccessKeySecret
            DefaultAcsClient client = initVodClient(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET);

            //2、创建获取视频凭证的request、response
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //3、向request对象里面设置视频id
            request.setVideoId(videoId);//去控制台复制视频id

            //4、调用初始化对象里面的方法，传递request，获取数据，赋值给 response
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //6、调用方法得到凭证
            String playAuth = response.getPlayAuth();
            //System.out.println("播放凭证 ==> " + playAuth);
            return R.ok().data("playAuth",playAuth);
        }
        catch (Exception e){
            throw new WzException(20001,"获取凭证失败");
        }
    }


}
