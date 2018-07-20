package com.sharex.token.api.util;

import java.util.Date;

public class TimeUtil {

    public static Date addMin(Date date, Integer min) {
        Long timeStamp = date.getTime() + min * 60 * 1000;
        return new Date(timeStamp);
    }

    public static Date addDay(Date date, Integer day) {
        Long timeStamp = date.getTime() + day * 24 * 60 * 60 * 1000;
        return new Date(timeStamp);
    }
}
