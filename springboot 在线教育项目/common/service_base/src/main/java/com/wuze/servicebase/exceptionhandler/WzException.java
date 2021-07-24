package com.wuze.servicebase.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-05 16:46:18
 */
@Data
@AllArgsConstructor //有参构造
@NoArgsConstructor  //无参构造
public class WzException extends RuntimeException {

    private Integer wzCode;//异常状态码
    private String wzMsg;//异常信息
}
