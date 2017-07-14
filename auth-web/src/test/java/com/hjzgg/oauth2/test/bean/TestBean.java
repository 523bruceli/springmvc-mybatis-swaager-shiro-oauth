package com.hjzgg.oauth2.test.bean;

import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hujunzheng on 2017/7/14.
 */
public class TestBean {

    public static void test1() {
        ExtendBean bean = new ExtendBean("hjz", 123, "code");

        PropertyDescriptor[] propertyDescriptors = BeanUtils.getPropertyDescriptors(bean.getClass());

        for(PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            System.out.println(propertyDescriptor.toString());
        }
    }

    public static void test2() {
        List<BaseBean> list = new ArrayList<>();
        BaseBean b1 = new BaseBean("hjz", 25);
        ExtendBean b2 = new ExtendBean("haha", 34, "code");

        list.add(b1);
        list.add(b2);
        System.out.println(list);

        list.remove(b1);
        System.out.println(list);

        list.remove(b2);
        System.out.println(list);
    }

    public static void main(String[] args) {
        test2();
    }
}

