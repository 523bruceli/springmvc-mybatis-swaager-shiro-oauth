package com.hjzgg.auth.controller;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
}
