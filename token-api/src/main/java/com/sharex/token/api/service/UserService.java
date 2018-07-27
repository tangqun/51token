package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.User;
import com.sharex.token.api.entity.UserFeedback;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.Feedback;
import com.sharex.token.api.mapper.UserFeedbackMapper;
import com.sharex.token.api.mapper.UserMapper;
import com.sharex.token.api.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserService {

    private static final Log logger = LogFactory.getLog(UserService.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserFeedbackMapper userFeedbackMapper;

    public RESTful feedback(Feedback feedback) {
        try {
            // 验证token
            if (StringUtils.isBlank(feedback.getToken())) {
                return RESTful.Fail(CodeEnum.TokenCannotBeNull);
            }
            if (!ValidateUtil.checkToken(feedback.getToken())) {
                return RESTful.Fail(CodeEnum.TokenFormatError);
            }

            if (StringUtils.isBlank(feedback.getContent())) {
                return RESTful.Fail(CodeEnum.FeedbackContentCannotBeNull);
            }
            if (feedback.getContent().length() > 200) {
                return RESTful.Fail(CodeEnum.FeedbackContentFormatError);
            }

            User user = userMapper.selectByToken(feedback.getToken());
            if (user == null) {
                return RESTful.Fail(CodeEnum.TokenInvalid);
            }
            if (user.getStatus() != 0) {
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
}
