package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserMapper {

    void insert(User user);
    User selectByMobileNum(String mobileNum);
    void updateToken(Map<String, Object> map);
    User selectByToken(String token);
    void updateKlineStatus(Map<String, Object> map);
}
