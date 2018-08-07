package com.sharex.token.api.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.exception.OverfrequencyException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ApiInterceptor {

    private static final Log logger = LogFactory.getLog(LogInterceptor.class);

    @Autowired
    private HashOperations<String, String, String> hashOperations;

    private ObjectMapper objectMapper = new ObjectMapper();

    // 切入点 execution: 切入点语法
    @Pointcut("execution(public * com.sharex.token.api.currency.huobi..*(..))")
    public void apiPointcut() {};

    @Around("apiPointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        // 接口调用前，判断调用次数
        // huobi
        //   api_calls_accounts_1533607584     0
        //   api_calls_accounts_1533607585     1
        long timestamp = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getName();

        Long ts = timestamp / 1000;

        String exchangeName = getExchangeName(className);

        String redisKey = "api_calls_" + methodName + "_" + ts.toString();

        String callsStr = hashOperations.get(exchangeName, redisKey);
        Long calls = 0L;
        if (null != callsStr) {
            calls = Long.valueOf(callsStr);
        }

        Long limit = getLimit(exchangeName, methodName);
        if (null == limit || calls > limit ) {
            throw new OverfrequencyException();
        }

        hashOperations.increment(exchangeName, redisKey, calls);


        Object result = joinPoint.proceed();

        logger.info("[***类名:" + className + " ,方法名:" + methodName + " ,共计消耗:" + (System.currentTimeMillis() - timestamp) + " ms ***]");

        return result;
    }

    private String getExchangeName(String className) {
        if (className.contains("huobi")) {
            return "huobi";
        } else if (className.contains("okex")) {
            return "okex";
        }
        return null;
    }

    private Long getLimit(String exchangenName, String methodName) {

        String key = exchangenName + "_" + methodName;
        return limitMap.get(key);
    }

    private static final Map<String, Long> limitMap = new HashMap<>();

    static {
        limitMap.put("huobi_trades", 1000L);
        limitMap.put("huobi_ticker", 1000L);
        limitMap.put("huobi_kline", 1000L);
        limitMap.put("huobi_accounts", 3L);
        limitMap.put("huobi_openOrders", 10L);
        limitMap.put("huobi_historyOrders", 10L);
        limitMap.put("huobi_entrustOrders", 10L);
        limitMap.put("huobi_placeOrder", 10L);
    }
}
