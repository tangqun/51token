package com.sharex.token.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtil {

    public static boolean checkMsgId(String msgId) {
        return checkUUID(msgId);
    }

    public static boolean checkToken(String token) {
        return checkUUID(token);
    }

    public static boolean checkUUID(String uuid) {
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");
            Matcher matcher = regex.matcher(uuid);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    public static boolean checkMobile(String mobile){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^1[3|4|5|7|8][0-9]\\d{4,8}$");
            Matcher matcher = regex.matcher(mobile);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }

    public static boolean checkSMSCode(String smsCode){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^\\d{6}$");
            Matcher matcher = regex.matcher(smsCode);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }


    public static boolean checkPassword(String str){
        boolean flag = false;
        try{
            Pattern regex = Pattern.compile("^([\u4e00-\u9fa50-9a-zA-Z]{6,16})$");
            Matcher matcher = regex.matcher(str);
            flag = matcher.matches();
        }catch(Exception e){
            flag = false;
        }
        return flag;
    }
}
