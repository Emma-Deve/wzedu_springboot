package com.wuze.educenter.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author wuze
 * @desc ...
 * @date 2021-01-29 02:31:56
 */
@Data
public class RegisterVo {
    @ApiModelProperty(value = "手机号")
    private String mobile;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "验证码")
    private String code;
}
