package com.hjzgg.common.util.request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/3/2.
 */
@SuppressWarnings("serial")
public class IpAnalizyResult implements Serializable {
    private Integer code;
    private Address data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Address getData() {
        return data;
    }

    public void setData(Address data) {
        this.data = data;
    }
}
