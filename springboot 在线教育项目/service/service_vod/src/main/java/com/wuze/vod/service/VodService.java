package com.wuze.vod.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-25 16:47:15
 */
public interface VodService {
    String getAliyunVodId(MultipartFile file) throws IOException;

    void removeAliyunVideoBatch(List videoList);
}
