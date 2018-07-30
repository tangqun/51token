package com.sharex.token.api.util;

import org.junit.Test;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Date;

public class TimeUtilTest {

    @Test
    public void add() {

        Date date = new Date();

        Date date2 = TimeUtil.addMin(date, 30);

        System.out.println(date);
        System.out.println(date2);
    }

    @Test
    public void instant() {

        ZoneOffset gmt = ZoneOffset.ofHours(8);

        System.out.println(Instant.now().atOffset(gmt));

        System.out.println(System.currentTimeMillis());
    }
}
