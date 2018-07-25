package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.UserCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserCurrencyMapper {

    void insert(UserCurrency userCurrency);
    void deleteByApiKey(String apiKey);
    List<UserCurrency> selectByApiKey(String apiKey);
}
