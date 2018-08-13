package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.ExchangeCurrency;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExchangeCurrencyMapper {

    List<ExchangeCurrency> selectList(String exchangeName);
}
