package com.wuze.eduservice.client;

import com.wuze.commonutils.R;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-26 18:11:44
 */
@Component
public class VodClientImpl implements VodClientInterface {
    @Override
    public R deleteAliyunVideo(String videoSourceId) {
        return R.error().message("删除单个视频失败（触发熔断器）");
    }

    @Override
    public R deleteAliyunVideoBatch(List<String> videoList) {
        return R.error().message("删除多个视频失败（触发熔断器）");
    }
}
