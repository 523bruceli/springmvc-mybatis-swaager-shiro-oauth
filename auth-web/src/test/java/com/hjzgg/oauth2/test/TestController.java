package com.hjzgg.oauth2.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/**
 * Created by hujunzheng on 2017/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-common.xml"})
@WebAppConfiguration
public class TestController {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    private void requestAndOutput(String url, String params) throws Exception {
        String responseString = mockMvc.perform(
                MockMvcRequestBuilders.post(url + "?access_token=b81d8adaeab4e41fe2125f08c8f7e9b5")
                        .accept(MediaType.ALL)
//                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                        .param("userId", "123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(params)
        )//.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())         //打印出请求和相应的内容
                .andReturn().getResponse().getContentAsString();

        System.out.println(responseString);
    }

    @Before
    public void  setUp( ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void test() throws Exception {
        JSONObject params = new JSONObject();
        params.put("userId", 123);
        params.put("page", 1);
        params.put("size", 1);

        requestAndOutput("/wallet/records", params.toJSONString());
    }
}
