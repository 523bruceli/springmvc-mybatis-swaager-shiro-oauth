package com.hjzgg.oauth2.test.bean;

/**
 * Created by hujunzheng on 2017/7/14.
 */
public class ExtendBean extends BaseBean{
    private String like;

    public ExtendBean(String name, Integer age, String like) {
        super(name, age);
        this.like = like;
    }


    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }
}
