package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.SMSCode;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.SMSCodeSend;
import com.sharex.token.api.mapper.SMSCodeMapper;
import com.sharex.token.api.util.AliSMSUtil;
import com.sharex.token.api.util.RandomUtil;
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

@Service
public class SMSCodeService {

    private static final Log logger = LogFactory.getLog(SMSCodeService.class);

    @Autowired
    private SMSCodeMapper smsCodeMapper;

    /**
     * 短信发送
     * @param smsCodeSend
     * @return
     * @author 唐群
     */
    public RESTful send(SMSCodeSend smsCodeSend) {

        try {

            if (StringUtils.isEmpty(smsCodeSend.getMobileNum())) {
                // 手机号不能为空
                return RESTful.Fail(CodeEnum.MobileNumCannotBeNull);
            }

            if (!ValidateUtil.checkMobile(smsCodeSend.getMobileNum())) {
                // 手机号格式错误
                return RESTful.Fail(CodeEnum.MobileNumFormatError);
            }

            Date date = new Date();
            Date dayStartTime = new Date(date.getYear(), date.getMonth(), date.getDate(), 0, 0, 0);
            Date dayEndTime = TimeUtil.addDay(dayStartTime, 1);

            Map<String, Object> map = new HashMap<>();
            map.put("mobileNum", smsCodeSend.getMobileNum());
            map.put("dayStartTime", dayStartTime);
            map.put("dayEndTime", dayEndTime);
            Integer count = smsCodeMapper.selectCountByDay(map);
            if (count >= 3) {
                // 每天允许3条
                return RESTful.Fail(CodeEnum.SMSOutOfLimit);
            }

            String code = RandomUtil.get();

            SMSCode smsCode = new SMSCode();
            smsCode.setMobileNum(smsCodeSend.getMobileNum());
            smsCode.setCode(code);
            smsCode.setCreateTime(date);
            smsCodeMapper.insert(smsCode);
            if (AliSMSUtil.send(smsCodeSend.getMobileNum(), code, smsCode.getId().toString())) {
                return RESTful.Success();
            }
            return RESTful.Fail(CodeEnum.SMSSendFail);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
