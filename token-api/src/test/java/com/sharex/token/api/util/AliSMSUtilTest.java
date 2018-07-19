package com.sharex.token.api.util;

import com.aliyuncs.exceptions.ClientException;
import org.junit.Test;

public class AliSMSUtilTest {

    @Test
    public void send() throws ClientException {
        AliSMSUtil.send("15210470906", "123456", "1");
    }
}
