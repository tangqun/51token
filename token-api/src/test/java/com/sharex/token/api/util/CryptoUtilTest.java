package com.sharex.token.api.util;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class CryptoUtilTest {

    @Test
    public void md5() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String access_key = "d2ccc964-813249e6-595ee6d8-574fb";

        Long created = System.currentTimeMillis();

        String secret_key = "bde5a51b-63d25997-a83a30c2-789ae";

        String s = "access_key=" + access_key + "&created=" + created + "&secret_key=" + secret_key;

        String sign = CryptoUtil.md5(s);

        s += "&sign=" + sign;

        String url = "https://api.huobipro.com?" + s.toLowerCase();

        String resp = HttpUtil.get(url);

        System.out.println(resp);
    }
}
