package com.sharex.token.api.util;

import org.junit.Test;

import java.util.Date;

public class TimeUtilTest {

    @Test
    public void addTest() {

        Date date = new Date();

        Date date2 = TimeUtil.addMin(date, 30);

        System.out.println(date);
        System.out.println(date2);
    }
}
