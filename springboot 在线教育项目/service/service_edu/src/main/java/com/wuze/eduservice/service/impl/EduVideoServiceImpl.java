package com.wuze.eduservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wuze.eduservice.entity.EduVideo;
import com.wuze.eduservice.mapper.EduVideoMapper;
import com.wuze.eduservice.service.EduVideoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author wuze
 * @since 2021-01-21
 */
@Service
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {

    //根据课程id删除小节（需要构建wrapper）
    @Override
    public void removeVideoByCourseId(String courseId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        baseMapper.delete(wrapper);
    }

    //根据课程id获取小节信息（获取视频id）
    @Override
    public List<String> getByCourseId(String courseId) {
        QueryWrapper<EduVideo> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id",courseId);
        List<EduVideo> eduVideos = baseMapper.selectList(wrapper);//返回小节集合

        //补充：将 List<EduVideo> 转换成 List<String>
        List<String> eduVideosString = new ArrayList<>();
        for (int i = 0; i < eduVideos.size(); i++) {
            EduVideo eduVideo = eduVideos.get(i);
            String videoSourceIdourceId = eduVideo.getVideoSourceId();//视频id（不是小节id）
            if(!StringUtils.isEmpty(videoSourceIdourceId)){
                eduVideosString.add(videoSourceIdourceId);
            }

        }
        return eduVideosString;
    }
}
