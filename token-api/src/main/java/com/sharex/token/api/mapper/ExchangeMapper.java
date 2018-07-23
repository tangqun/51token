package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.Exchange;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface ExchangeMapper {

    List<Exchange> selectEnabled();
    Exchange selectEnabledByShortName(String shortName);
}
