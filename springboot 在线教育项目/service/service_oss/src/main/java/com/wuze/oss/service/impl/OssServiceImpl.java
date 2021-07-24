package com.wuze.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.wuze.oss.service.OssService;
import com.wuze.oss.utils.ConstantPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 01:41:15
 */
@Service
public class OssServiceImpl implements OssService {

    //上传头像到oss
    //代码具体实现在这里
    //返回图片路径
    @Override
    public String uploadFileAvatar(MultipartFile file) {
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = ConstantPropertiesUtils.END_POINT;
        // 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId =ConstantPropertiesUtils.KEY_ID;
        String accessKeySecret = ConstantPropertiesUtils.KEY_SECRET;
        String bucketName = ConstantPropertiesUtils.BUCKET;

        try{
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

            // 上传文件流。
            InputStream inputStream = file.getInputStream();

            //获取文件名称
            String fileName=file.getOriginalFilename();

            /*补充：
            * 解决了重复上传同一文件覆盖问题（两种方法）
            * */
            //因为uuid生成的字符串包含横杆-，replaceAll是将所有横杆-替换成空字符
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            //第一次拼接，例如：hcasdhc01.jpg
            fileName=uuid+fileName;

            //利用日期工具类joda
            String datePath = new DateTime().toString("yyyy/MM/dd");
            //第二次拼接，例如：2021/01/20/hcasdhc01.jpg
            fileName=datePath+"/"+fileName;


            //调用oss方法实现上传
            //参数1、bucketName
            //参数2、上传到oss的文件路径和文件名称  aa/bb/1.jpg
            //参数3、上传文件输入流
            ossClient.putObject(bucketName, fileName, inputStream);

            // 关闭OSSClient。
            ossClient.shutdown();

            //返回图片路径（阿里云Oss）
            //格式：https://onlineedu-wz.oss-cn-hangzhou.aliyuncs.com/01.jpg
            String url="https://"+bucketName+"."+endpoint+"/"+fileName;
            return url;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
}
