package com.hjzgg.controller;

import com.hjzgg.form.DataReqForm;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("index")
public class IndexController {

    @ApiOperation(value = "index controller", notes = "测试一下")
    @RequestMapping(value = "hello", method = RequestMethod.GET)
    @ResponseBody
    public String hello(@ApiParam(name = "name", value = "姓名", required = true) @RequestParam String name,
                        @ApiParam(name = "age", value = "年龄", required = false) @RequestParam(required = false) String age) {
        return "hello " + name + (StringUtils.isEmpty(age) ? "." : ", your age is " + age + ".");
    }


    @ApiOperation(value = "requestBodyDemo", notes = "requestBody swagger api示例")
    @RequestMapping(value = "requestBodyDemo", method = RequestMethod.GET)
    @ResponseBody
    public String requestBodyDemo(@Valid @RequestBody
                                      @ApiParam(name = "dataReqForm", value = "接口请求表单", required = true)
                                              DataReqForm dataReqForm, BindingResult br) {
        return dataReqForm.toString();
    }
}
