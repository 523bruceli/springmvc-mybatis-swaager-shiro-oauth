package com.hjzgg.auth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunzheng on 2017/6/22.
 */
@Configuration
@PropertySource("classpath:conf/config.properties")
public class SystemConfig {

    public static class User {
        public static String NAME;
        public static int AGE;
        public static int USER_ID;
        public static String ADDRESS;
    }

    @Autowired
    private Environment env;

    @Bean
    public SystemConfig oauthSystemConfig() throws IllegalAccessException {
        setStaticPropertiesValue(SystemConfig.class);
        Class<?>[] clazzs = SystemConfig.class.getClasses();
        for (Class clazz: clazzs) {
            setStaticPropertiesValue(clazz);
        }
        return null;
    }

    private void setStaticPropertiesValue(Class<?> clazz) throws IllegalAccessException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String key = field.getName().toLowerCase().replace("_", ".");
            String value = env.getProperty(key);

            if (StringUtils.isEmpty(value)) {
                continue;
            }

            if (field.getType() == List.class) {
                String[] values = value.split(",");
                List<String> vlist = new ArrayList<String>();
                for (String tvalue : values) {
                    if (!StringUtils.isEmpty(tvalue)) {
                        vlist.add(tvalue);
                    }
                }
                field.set(null, vlist);
            }

            if (field.getType() == String[].class) {
                String[] values = value.split(",");
                List<String> vlist = new ArrayList<String>();
                for (String tvalue : values) {
                    if (!StringUtils.isEmpty(tvalue)) {
                        vlist.add(tvalue);
                    }
                }
                field.set(null, vlist.toArray(new String[] {}));
            }

            if (field.getType() == String.class) {
                field.set(null, value);
            }
            if (field.getType() == Integer.class) {
                field.set(null, Integer.valueOf(value));
            }
            if (field.getType() == Float.class) {
                field.set(null, Float.valueOf(value));
            }
            if (field.getType() == Double.class) {
                field.set(null, Double.valueOf(value));
            }
        }
    }
}
