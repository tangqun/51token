package com.sharex.token.api.mapper;

import com.sharex.token.api.entity.SMSCode;

import java.util.Map;

public interface SMSCodeMapper {

    int insert(SMSCode smsCode);
    SMSCode selectByMobileNum(Map<String, Object> map);
}
