package com.sharex.token.api.util;

public class StringUtil {

    public static String ReplaceByMosaic(String mobileNum) {

         return mobileNum.substring(0, 3) + "****" + mobileNum.substring(7, 11);
    }
}
