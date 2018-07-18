package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.User;
import com.sharex.token.api.entity.req.PasswordLogin;
import com.sharex.token.api.entity.req.SMSCodeLogin;
import com.sharex.token.api.mapper.UserMapper;
import com.sharex.token.api.util.ValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 短信登陆
     * @param smsCodeLogin
     * @return
     */
    public RESTful loginBySMSCode(SMSCodeLogin smsCodeLogin) {

        if (!StringUtils.isEmpty(smsCodeLogin.getMobileNum())) {
            // 手机号不能为空
        }

        if (!ValidateUtil.checkMobile(smsCodeLogin.getMobileNum())) {
            // 手机号格式错误
        }

        if (!StringUtils.isEmpty(smsCodeLogin.getSmsCode())) {
            // 短信码不能为空
        }

        if (!ValidateUtil.checkSMSCode(smsCodeLogin.getSmsCode())) {
            // 短信码格式错误
        }

        // 验证短信码

        User user = userMapper.selectByMobileNum(smsCodeLogin.getMobileNum());

        String token = UUID.randomUUID().toString();
        Date date = new Date();

        if (user == null) {
            // 插入用户
            user = new User();
            user.setMobileNum(smsCodeLogin.getMobileNum());
            user.setToken(token);
            user.setLastLoginTime(date);
            user.setCreateTime(date);
            userMapper.insert(user);

            // resp level 1 data
            Map<String, Object> map = new HashMap<>();

            // resp level 2 data
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mobileNum", smsCodeLogin.getMobileNum());
            map2.put("status", 0);
            map2.put("token", token);
            map.put("user", map2);

            return RESTful.Success(map);
        } else {
            // 判断是否冻结
            if (user.getStatus() != 0) {
                // 被冻结
            }

            // 更新token
            Map<String, Object> tokenMap = new HashMap<>();
            tokenMap.put("newToken", token);
            tokenMap.put("lastLoginTime", date);
            tokenMap.put("oldToken", user.getToken());
            userMapper.updateToken(tokenMap);

            // resp level 1 data
            Map<String, Object> map = new HashMap<>();

            // resp level 2 data
            Map<String, Object> map2 = new HashMap<>();
            map2.put("mobileNum", smsCodeLogin.getMobileNum());
            map2.put("status", 0);
            map2.put("token", token);
            map.put("user", map2);

            return RESTful.Success(map);
        }
    }

    /**
     * 密码登陆
     * @param passwordLogin
     * @return
     */
    public RESTful loginByPassword(PasswordLogin passwordLogin) {

        return null;
    }
}
