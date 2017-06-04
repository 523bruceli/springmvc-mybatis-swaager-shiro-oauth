package com.hjzgg.auth.service;

import com.hjzgg.auth.domain.dao.LightUserDao;
import com.hjzgg.auth.domain.dto.LightUserResult;
import com.hjzgg.auth.domain.entity.LightUser;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component(value = "userApi")
public class UserApiImpl {

    @Resource
    private LightUserDao lightUserDao;

    public LightUserResult queryUserByClientId(String clientId) {

        LightUser lightUser = lightUserDao.findByClientId(clientId);
        if (null == lightUser) {
            return null;
        }

        LightUserResult lightUserResult = new LightUserResult();
        BeanUtils.copyProperties(lightUser, lightUserResult);
        return lightUserResult;
    }
}
