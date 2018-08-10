package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.User;
import com.sharex.token.api.entity.UserFeedback;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.Feedback;
import com.sharex.token.api.entity.req.SwitchKline;
import com.sharex.token.api.mapper.UserFeedbackMapper;
import com.sharex.token.api.mapper.UserMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    private static final Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    public RESTful feedback(String token, Feedback feedback) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (0 != user.getStatus()) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            Date date = new Date();
            UserFeedback userFeedback = new UserFeedback();
            userFeedback.setContent(feedback.getContent());
            userFeedback.setUserId(user.getId());
            userFeedback.setCreateTime(date);

            userFeedbackMapper.insert(userFeedback);

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    public RESTful switchKline(String token, SwitchKline switchKline) {
        try {

            User user = userMapper.selectByToken(token);
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (0 != user.getStatus()) {
                return RESTful.Fail(CodeEnum.AccountHasBeenFrozen);
            }

            if (switchKline.getKlineStatus().equals(0)) {
                // 想要修改为 红涨绿跌
                if (user.getKlineStatus().equals(switchKline.getKlineStatus())) {
                    // 无须修改
                    return RESTful.Fail(CodeEnum.KlineStatusEqualsInDB);
                }

                updateKlineStatus(0, token);

                return RESTful.Success();
            } else {

                // 想要修改为 绿涨红跌
                if (!user.getKlineStatus().equals(0)) {
                    // 无须修改
                    return RESTful.Fail(CodeEnum.KlineStatusEqualsInDB);
                }

                updateKlineStatus(1, token);

                return RESTful.Success();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }

    private void updateKlineStatus(Integer klineStatus, String token) {

        Date date = new Date();
        Map<String, Object> map = new HashMap<>();
        map.put("klineStatus", klineStatus);
        map.put("updateTime", date);
        map.put("token", token);
        userMapper.updateKlineStatus(map);
    }
}
