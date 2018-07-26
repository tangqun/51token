package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.UserApi;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserApiMapper {

    void insert(UserApi userApi);
    void updateStatus(Map<String, Object> map);
    UserApi selectByType(Map<String, Object> map);
    List<UserApi> selectEnabledByUserId(Integer userId);
}
