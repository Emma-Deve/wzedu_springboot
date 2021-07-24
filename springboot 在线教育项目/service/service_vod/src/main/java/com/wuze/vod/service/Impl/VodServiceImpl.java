package com.wuze.vod.service.Impl;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.wuze.servicebase.exceptionhandler.WzException;
import com.wuze.vod.service.VodService;
import com.wuze.vod.utils.ConstantVodUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;

import static com.wuze.vod.utils.InitVodClient.initVodClient;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-25 16:47:33
 */
@Service
public class VodServiceImpl implements VodService {

    /**
     * 流式上传接口
     */
    @Override
    public String getAliyunVodId(MultipartFile file){

        try {

        //fileName 上传文件原始名称
        String fileName=file.getOriginalFilename(); //获取上传文件名称


        //title 上传之后显示名称
        //01.02.03.mp4 --> 01.02.03
        //abc.mp4 --> abc
        String title=fileName.substring(0,fileName.lastIndexOf("."));

        //inputStream 上传文件输入流
        InputStream inputStream = file.getInputStream();

        //通过 ConstantVodUtils类.xxx 获取 keyid 和 keysecret
        UploadStreamRequest request = new UploadStreamRequest(ConstantVodUtils.KEY_ID, ConstantVodUtils.KEY_SECRET, title, fileName, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        //实现上传
        UploadStreamResponse response = uploader.uploadStream(request);

        String responseId=null; //定义responseId接收返回的视频id

        if (response.isSuccess()) {
            responseId = response.getVideoId();
        } else { //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            responseId = response.getVideoId();
        }
        return responseId;

        }catch (Exception e){
            throw new WzException(20001,"上传视频失败");
        }
    }

    //批量删除阿里云视频
    @Override
    public void removeAliyunVideoBatch(List videoList) {
        try {
            //1、初始化对象
            DefaultAcsClient client = initVodClient("LTAI4GAL4KRB17VWSeVv2s5g", "2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7");
            //2、创建删除视频request对象
            DeleteVideoRequest request = new DeleteVideoRequest();

            //补充：将List分割成 1，2，3的形式，才能传入下面的方法
            //将list转换成数组，再用逗号分隔
            String aliyunMoreVideo = StringUtils.join(videoList.toArray(), ",");

            //3、向request设置视频id，支持传入多个视频ID，多个用逗号分隔（1，2，3）
            request.setVideoIds(aliyunMoreVideo);

            //4、调用初始化对象的方法实现删除
            client.getAcsResponse(request);

        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
            throw new WzException(20001,"批量删除视频失败");
        }
    }
}




