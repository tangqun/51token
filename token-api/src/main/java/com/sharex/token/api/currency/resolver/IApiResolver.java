package com.sharex.token.api.currency.resolver;

import com.sharex.token.api.entity.RemoteSyn;

public interface IApiResolver {

    RemoteSyn getKline(String symbol, String type) throws Exception;

    RemoteSyn getTrades(String symbol) throws Exception;
}
