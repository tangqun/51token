package com.sharex.token.api.service;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.SMSCode;
import com.sharex.token.api.entity.enums.CodeEnum;
import com.sharex.token.api.entity.req.SMSCodeRemove;
import com.sharex.token.api.entity.req.SMSCodeSend;
import com.sharex.token.api.mapper.SMSCodeMapper;
import com.sharex.token.api.util.AliSMSUtil;
import com.sharex.token.api.util.RandomUtil;
import com.sharex.token.api.util.TimeUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public RESTful remove(SMSCodeRemove smsCodeRemove) {
        try {

            smsCodeMapper.delete(smsCodeRemove.getMobileNum());

            return RESTful.Success();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
