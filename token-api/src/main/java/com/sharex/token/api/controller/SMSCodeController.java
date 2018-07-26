package com.sharex.token.api.controller;

import com.sharex.token.api.entity.RESTful;
import com.sharex.token.api.entity.req.SMSCodeRemove;
import com.sharex.token.api.entity.req.SMSCodeSend;
import com.sharex.token.api.service.SMSCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api("短信")
@RequestMapping("/smsCode")
@RestController
public class SMSCodeController {

    @Autowired
    private SMSCodeService smsCodeService;

    @ApiOperation("发送短信")
    @RequestMapping(value = "/send", method = RequestMethod.POST)
    public RESTful send(@RequestBody SMSCodeSend smsCodeSend) {
        return smsCodeService.send(smsCodeSend);
    }

    @ApiOperation("清除短信")
    @RequestMapping(value = "/remove", method = RequestMethod.POST)
    public RESTful remove(@RequestBody SMSCodeRemove smsCodeRemove) {

        return smsCodeService.remove(smsCodeRemove);
    }
}
