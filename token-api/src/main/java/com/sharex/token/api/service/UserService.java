package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.SMSCode;
import com.sharex.token.api.entity.User;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.LoginPassword;
import com.sharex.token.api.entity.req.LoginSMSCode;
import com.sharex.token.api.mapper.SMSCodeMapper;
import com.sharex.token.api.mapper.UserMapper;
import com.sharex.token.api.util.CryptoUtil;
import com.sharex.token.api.util.StringUtil;
import com.sharex.token.api.util.TimeUtil;
import com.sharex.token.api.util.ValidateUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

    @Autowired
    private SMSCodeMapper smsCodeMapper;

    private Log logger = LogFactory.getLog(UserService.class);

    /**
     * 短信登陆
     * @param loginSMSCode
     * @return
     * @author 唐群
     */
    public RESTful loginBySMSCode(LoginSMSCode loginSMSCode) {

        try {

            if (StringUtils.isEmpty(loginSMSCode.getMobileNum())) {
                // 手机号不能为空
                return RESTful.Fail(CodeEnum.MobileNumCannotBeNull);
            }

            if (!ValidateUtil.checkMobile(loginSMSCode.getMobileNum())) {
                // 手机号格式错误
                return RESTful.Fail(CodeEnum.MobileNumFormatError);
            }

            if (StringUtils.isEmpty(loginSMSCode.getSmsCode())) {
                // 短信码不能为空
                return RESTful.Fail(CodeEnum.SMSCodeCannotBeNull);
            }

            if (!ValidateUtil.checkSMSCode(loginSMSCode.getSmsCode())) {
                // 短信码格式错误
                return RESTful.Fail(CodeEnum.SMSCodeFormaError);
            }

            // 当前时间
            Date date = new Date();

            // 验证短信码
            Map<String, Object> smsCodeMap = new HashMap<>();
            smsCodeMap.put("mobileNum", loginSMSCode.getMobileNum());
            smsCodeMap.put("createTime", TimeUtil.addMin(date, 30));
            SMSCode smsCode = smsCodeMapper.selectByMobileNum(smsCodeMap);
            if (smsCode == null) {
                // 验证码不存在
                return RESTful.Fail(CodeEnum.SMSCodeNotInDB);
            }
            if (smsCode.getStatus() != 0) {
                // 验证码已被使用
                return RESTful.Fail(CodeEnum.SMSCodeHasBeenUsed);
            }
            if (!loginSMSCode.getSmsCode().equals(smsCode.getCode())) {
                // 验证码不正确
                return RESTful.Fail(CodeEnum.SMSCodeNotEqualsInDB);
            }

            // 标记短信码失效
            smsCodeMapper.update(smsCode.getId());

            // 验证用户
            User user = userMapper.selectByMobileNum(loginSMSCode.getMobileNum());

            String token = UUID.randomUUID().toString();

            if (user == null) {
                // 插入用户
                user = new User();
                user.setMobileNum(loginSMSCode.getMobileNum());
                user.setToken(token);
                user.setLastLoginTime(date);
                user.setCreateTime(date);
                userMapper.insert(user);

                // resp level 1 data
                Map<String, Object> map = new HashMap<>();

                // resp level 2 data
                Map<String, Object> map2 = new HashMap<>();
                map2.put("mobileNum", StringUtil.ReplaceByMosaic(loginSMSCode.getMobileNum()));
                map2.put("status", 0);
                map2.put("token", token);
                map.put("user", map2);

                return RESTful.Success(map);
            } else {
                // 判断是否冻结
                if (user.getStatus() != 0) {
                    // 被冻结
                    return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
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
                map2.put("mobileNum", StringUtil.ReplaceByMosaic(loginSMSCode.getMobileNum()));
                map2.put("status", 0);
                map2.put("token", token);
                map.put("user", map2);

                return RESTful.Success(map);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    /**
     * 密码登陆
     * @param loginPassword
     * @return
     * @author 唐群
     */
    public RESTful loginByPassword(LoginPassword loginPassword) {

        if (StringUtils.isEmpty(loginPassword.getMobileNum())) {
            // 手机号不能为空
            return RESTful.Fail(CodeEnum.MobileNumCannotBeNull);
        }

        if (!ValidateUtil.checkMobile(loginPassword.getMobileNum())) {
            // 手机号格式错误
            return RESTful.Fail(CodeEnum.MobileNumFormatError);
        }

        if (StringUtils.isEmpty(loginPassword.getPassword())) {
            // 短信码不能为空
            return RESTful.Fail(CodeEnum.SMSCodeCannotBeNull);
        }

        if (!ValidateUtil.checkPassword(loginPassword.getPassword())) {
            // 短信码格式错误
            return RESTful.Fail(CodeEnum.SMSCodeFormaError);
        }

        User user = userMapper.selectByMobileNum(loginPassword.getMobileNum());
        if (user == null) {
            // 不能提示未注册，提示用户名密码错误
            return RESTful.Fail(CodeEnum.UserNotInDB);
        }

        // 密码MD5相等
        if (CryptoUtil.md5(loginPassword.getPassword()).toLowerCase().equals(user.getPassword())) {

            Date date = new Date();

            String token = UUID.randomUUID().toString();

            // 判断是否冻结
            if (user.getStatus() != 0) {
                // 被冻结
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
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
            map2.put("mobileNum", StringUtil.ReplaceByMosaic(loginPassword.getMobileNum()));
            map2.put("status", 0);
            map2.put("token", token);
            map.put("user", map2);

            return RESTful.Success(map);
        }

        // 用户名密码错误，与用户不存在提示相同
        return RESTful.Fail(CodeEnum.PasswordNotEqualsInDB);
    }
}
