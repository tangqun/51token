package com.sharex.token.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.okex.response.UserInfo;
import com.sharex.token.api.currency.okex.stock.IStockRestApi;
import com.sharex.token.api.currency.okex.stock.impl.StockRestApi;
import com.sharex.token.api.entity.RESTful;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class OkexService {

    private static final String AppKey = "63310fb3-de7d-435a-952a-57ddb853d8a8";
    private static final String AppSecret = "74CBAE8EB95C5FDFFE9C31DB35FACDAB";

    private IStockRestApi stockRestApi = new StockRestApi(AppKey, AppSecret);

    private static final Log logger = LogFactory.getLog(OkexService.class);

    public RESTful getUserinfo() {
        try {

            String result = stockRestApi.userinfo();
            ObjectMapper objectMapper = new ObjectMapper();
            UserInfo userInfo = objectMapper.readValue(result, UserInfo.class);

//            if (userInfo.getResult()) {
//
//            }

            return RESTful.Success(userInfo.getInfo());

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return RESTful.SystemException();
        }
    }
}
