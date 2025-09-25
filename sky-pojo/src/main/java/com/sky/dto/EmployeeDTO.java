package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("新增员工时传递的数据模型")
public class EmployeeDTO implements Serializable {
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户昵称")
    private String username;

    @ApiModelProperty("用户姓名")
    private String name;

    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("身份证号码")
    private String idNumber;

}
