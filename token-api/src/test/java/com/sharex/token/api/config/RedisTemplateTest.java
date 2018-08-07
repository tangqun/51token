package com.sharex.token.api.config;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    @Test
    public void increment() {

        Long ts = System.currentTimeMillis() / 1000;

        String callsStr = hashOperations.get("huobi", "api_accounts_calls" + ts.toString());

        Long calls = 0L;
        if (null != callsStr) {
            calls = Long.valueOf(callsStr);
        }

        hashOperations.increment("huobi", "api_accounts_calls" + ts.toString(), calls);
    }
}
