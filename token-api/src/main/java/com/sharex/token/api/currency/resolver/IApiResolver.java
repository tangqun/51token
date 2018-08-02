package com.sharex.token.api.currency.resolver;

import com.sharex.token.api.entity.RemoteSyn;
import com.sharex.token.api.entity.UserCurrency;

import java.util.Map;

public interface IApiResolver {

    RemoteSyn getKline(String symbol, String type) throws Exception;

    RemoteSyn getTrades(String symbol) throws Exception;

    Map<String, UserCurrency> accounts(Integer userId) throws Exception;
}
