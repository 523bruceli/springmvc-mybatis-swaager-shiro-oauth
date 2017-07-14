package com.hjzgg.oauth2.test.bean;

/**
 * Created by hujunzheng on 2017/7/14.
 */
public class BaseBean {
    private String name;
    private Integer age;

    public BaseBean(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
