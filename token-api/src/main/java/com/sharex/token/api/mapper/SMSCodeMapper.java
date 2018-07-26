package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.SMSCode;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Mapper
@Repository
public interface SMSCodeMapper {

    void insert(SMSCode smsCode);
    void update(Integer id);
    void delete(String mobileNum);
    SMSCode selectByMobileNum(Map<String, Object> map);
    Integer selectCountByDay(Map<String, Object> map);
}
