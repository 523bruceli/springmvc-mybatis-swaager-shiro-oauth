package com.hjzgg.oauth2.test;

import com.hjzgg.auth.config.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by hujunzheng on 2017/6/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-common.xml", "classpath:spring/spring-datasource.xml", "classpath:mapper/*.xml"}) // 加载配置
@WebAppConfiguration
public class TestService {

    @Test
    public void test() {
       System.out.println(SystemConfig.User.ADDRESS);
    }
}
