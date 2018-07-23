package com.sharex.token.api.service;

import com.sharex.token.api.currency.huobi.api.ApiClient;
import com.sharex.token.api.entity.RESTful;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class HuoBiService {

    private static final String AppKey = "d2ccc964-813249e6-595ee6d8-574fb";
    private static final String AppSecret = "bde5a51b-63d25997-a83a30c2-789ae";

    private static final Log logger = LogFactory.getLog(HuoBiService.class);

    @Bean
    private ApiClient getApiClient() {
        return new ApiClient(AppKey, AppSecret);
    }

    public RESTful getSymbols() {
        try {

            return RESTful.Success(getApiClient().getSymbols());
        } catch (Exception e) {
            return RESTful.SystemException();
        }
    }

    public RESTful getAccounts() {
        try {

            return RESTful.Success(getApiClient().getAccounts());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
