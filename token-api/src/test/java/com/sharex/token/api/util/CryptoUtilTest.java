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

        String resp = HttpUtil.get(url, null);

        System.out.println(resp);
    }

    @Test
    public void hmacSha256() throws Exception {

        String str = "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.1&recvWindow=5000&timestamp=1499827319559";

        String apiSecret = "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j";


        // yNtWglrnHW15RHhJ5hcRX0qSD6Ks3KsrBTxLKDi9a3E=
        // c8db56825ae71d6d79447849e617115f4a920fa2acdcab2b053c4b2838bd6b71

//        String sign = CryptoUtil.hmacSha256(apiSecret, str);

        String sign = CryptoUtil.hmacSha256(apiSecret, str);

        System.out.println(sign);
    }
}
