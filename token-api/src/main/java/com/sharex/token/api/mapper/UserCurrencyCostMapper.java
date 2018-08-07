package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.UserCurrencyCost;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface UserCurrencyCostMapper {

    void insert(UserCurrencyCost userCurrencyCost);
    UserCurrencyCost selectEntity(Map<String, Object> map);
}
