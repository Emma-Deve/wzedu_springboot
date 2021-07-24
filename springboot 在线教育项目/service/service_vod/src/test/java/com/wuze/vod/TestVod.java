package com.wuze.vod;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadVideoRequest;
import com.aliyun.vod.upload.resp.UploadVideoResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.wuze.servicebase.exceptionhandler.WzException;

import static com.wuze.vod.InitVod.initVodClient;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-25 13:39:57
 */
public class TestVod {
    /////////////////////////////////////////////////////////////
    ////////////  获取播放凭证 方法一 //////////////////////////////
    ////////////////////////////////////////////////////////////

    /*获取播放凭证函数*/
    public static GetVideoPlayAuthResponse getVideoPlayAuth(DefaultAcsClient client) throws Exception {
        //2、创建获取视频凭证的request
        GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
        //3、向request对象里面设置视频id
        request.setVideoId("caa2c067fc264d0694fe0f1bf2f6d4bd");//去控制台复制视频id
        //4、调用初始化对象里面的方法，传递request，获取数据
        return client.getAcsResponse(request);
    }

    //根据视频id获取视频播放凭证
    public static void getPlayAuth() throws ClientException {
        //initVodClient 参数1：AccessKeyId、参数2：AccessKeySecret
        //1、创建初始化对象
        DefaultAcsClient client = initVodClient("LTAI4GAL4KRB17VWSeVv2s5g", "2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7");

        //2、创建获取视频凭证的response
        GetVideoPlayAuthResponse response = new GetVideoPlayAuthResponse();
        try {
            //3、
            response = getVideoPlayAuth(client);
            //播放凭证
            System.out.print("PlayAuth = " + response.getPlayAuth() + "\n");
            //VideoMeta信息
            System.out.print("VideoMeta.Title = " + response.getVideoMeta().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }



    /////////////////////////////////////////////////////////////
    ////////////  获取播放凭证 方法二 (更好！)//////////////////////////////
    ////////////////////////////////////////////////////////////
    public static void getPlayAuth2() {
        try {
            //1、创建初始化对象
            //initVodClient 参数1：AccessKeyId、参数2：AccessKeySecret
            DefaultAcsClient client = initVodClient("LTAI4GAL4KRB17VWSeVv2s5g", "2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7");

            //2、创建获取视频凭证的request、response
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();

            //3、向request对象里面设置视频id
            request.setVideoId("caa2c067fc264d0694fe0f1bf2f6d4bd");//去控制台复制视频id

            //4、调用初始化对象里面的方法，传递request，获取数据，赋值给 response
            GetVideoPlayAuthResponse response = client.getAcsResponse(request);

            //6、调用方法得到凭证
            String playAuth = response.getPlayAuth();
            System.out.println("播放凭证 ==> " + playAuth);
        }
        catch (Exception e){
            throw new WzException(20001,"获取凭证失败");
        }
    }






    ////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////

    //本地上传视频
    /**
     * 本地文件上传接口
     *
     * @param accessKeyId
     * @param accessKeySecret
     * @param title
     * @param fileName
     */
    private static void testUploadVideo(String accessKeyId, String accessKeySecret, String title, String fileName) {
        UploadVideoRequest request = new UploadVideoRequest(accessKeyId, accessKeySecret, title, fileName);
         //可指定分片上传时每个分片的大小，默认为2M字节
        request.setPartSize(2 * 1024 * 1024L);
         //可指定分片上传时的并发线程数，默认为1，(注：该配置会占用服务器CPU资源，需根据服务器情况指定）
        request.setTaskNum(1);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadVideoResponse response = uploader.uploadVideo(request);
        System.out.print("RequestId=" + response.getRequestId() + "\n");  //请求视频点播服务的请求ID
        if (response.isSuccess()) {
            System.out.print("VideoId=" + response.getVideoId() + "\n");
        } else {
             //如果设置回调URL无效，不影响视频上传，可以返回VideoId同时会返回错误码。其他情况上传失败时，VideoId为空，此时需要根据返回错误码分析具体错误原因
            System.out.print("VideoId=" + response.getVideoId() + "\n");
            System.out.print("ErrorCode=" + response.getCode() + "\n");
            System.out.print("ErrorMessage=" + response.getMessage() + "\n");
        }
    }



    /*以下为调用示例*/
    public static void main(String[] argv) throws ClientException {
        //testUploadVideo("LTAI4GAL4KRB17VWSeVv2s5g","2sfnpP9NOjnzxAOY0OHPCC2dNV5Es7","标题1","C:/Users/w1396/Desktop/vod1.mp4");  //本地上传视频
        //获取阿里云视频播放凭证测试
        getPlayAuth2();
    }

}
