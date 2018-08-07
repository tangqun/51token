package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.OrderMsg;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface OrderMsgMapper {

    void insert(OrderMsg orderMsg);
    Integer selectCount(String msgId);
    void delete(String msgId);
}
