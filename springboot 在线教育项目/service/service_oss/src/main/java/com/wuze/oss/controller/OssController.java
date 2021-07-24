package com.wuze.oss.controller;

import com.wuze.commonutils.R;
import com.wuze.oss.service.OssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 01:37:22
 */
@RestController
@RequestMapping("/eduoss/fileoss")
@CrossOrigin    //解决跨域问题
public class OssController {
    @Autowired
    private OssService ossService;

    //上传头像
    //Post提交
    //返回图像文件路径url，因为数据库保存的头像属性avatar是保存url地址。
    @PostMapping
    public R uploadOssFile(MultipartFile file){
        //获取上传文件 MultipartFile
        //返回上传到oss的路径
        String url = ossService.uploadFileAvatar(file);
        return R.ok().data("url",url);//我们前端获取值也用名称 url
    }
}
