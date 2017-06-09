package com.hjzgg.form;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by hujunzheng on 2017/6/9.
 */

@ApiModel(value = "dataReqForm", description = "接口请求表单模型")
public class DataReqForm {
    @ApiModelProperty(value = "年龄", required = true)
    @NotNull(message = "age is null")
    private Integer age;

    @ApiModelProperty(value = "姓名", required = true)
    @NotBlank(message = "name is blank")
    private String name;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "DataReqForm{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
