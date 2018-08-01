package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.UserCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface UserCurrencyMapper {

    void insert(UserCurrency userCurrency);
    void delete(Map<String, Object> map);
    List<UserCurrency> selectByApiKey(String apiKey);
    UserCurrency selectEntity(Map<String, Object> map);
}
