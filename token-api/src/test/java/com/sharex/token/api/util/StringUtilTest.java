package com.sharex.token.api.util;

import org.junit.Test;

public class StringUtilTest {

    @Test
    public void ReplaceByMosaicTest() {

        String mobileNum = StringUtil.ReplaceByMosaic("15210470906");

        System.out.println(mobileNum);
    }
}
