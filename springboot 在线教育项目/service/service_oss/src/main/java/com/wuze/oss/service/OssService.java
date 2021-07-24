package com.wuze.oss.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-20 01:41:27
 */
public interface OssService{
    String uploadFileAvatar(MultipartFile file);
}
