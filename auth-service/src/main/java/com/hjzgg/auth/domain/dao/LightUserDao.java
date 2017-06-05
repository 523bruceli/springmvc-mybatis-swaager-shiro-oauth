package com.hjzgg.auth.domain.dao;

import com.hjzgg.auth.domain.entity.LightUser;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface LightUserDao {

    int deleteById(Long userId);

    int insert(LightUser record);

    int insertSelective(LightUser record);

    LightUser findOne(Long userId);

    LightUser findUserByName(String username);

    int updateByIdSelective(LightUser record);

    @Select("select * from light_user where client_id=#{clientId}")
    @ResultMap(value = "BaseResultMap")
    LightUser findByClientId(String clientId);


}