package com.wuze.edumsm.service;

import java.util.Map;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-28 18:43:55
 */
public interface MsmService {
    boolean sendMessage(Map<String, Object> param, String phone);
}
