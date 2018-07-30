package com.sharex.token.api.util;

import org.junit.Test;

public class HttpUtilTest {

    @Test
    public void get() {

        String res = HttpUtil.get("https://api.huobipro.com/v1/common/timestamp", null);

        System.out.println(res);
    }
}
