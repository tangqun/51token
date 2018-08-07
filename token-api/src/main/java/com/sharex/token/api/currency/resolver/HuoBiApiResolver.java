package com.sharex.token.api.currency.resolver;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.ApiClinetFactory;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.*;
import com.sharex.token.api.entity.*;
import com.sharex.token.api.exception.KlineSynException;
import com.sharex.token.api.exception.NetworkException;
import com.sharex.token.api.exception.PlaceOrderPostException;
import com.sharex.token.api.exception.TradesSynException;
import com.sharex.token.api.service.RemoteSynService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class HuoBiApiResolver implements IApiResolver {

    private static final Log logger = LogFactory.getLog(RemoteSynService.class);

//    private String apiKey;
//    private String apiSecret;
//    private IApiClient apiClient;
//
//    public HuoBiApiResolver() {
//        this.apiClient = new HuoBiApiClient();
//    }
//
//    public HuoBiApiResolver(String apiKey, String apiSecret) {
//        this.apiKey = apiKey;
//        this.apiSecret = apiSecret;
//        this.apiClient = new HuoBiApiClient(apiKey, apiSecret);
//    }

    @Autowired
    private HuoBiApiClient huoBiApiClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RemoteSyn getKline(String symbol, String type) throws Exception {

        //
//        IApiClient apiClient = ApiClinetFactory.getInstence("huobi");

        // 同步redis，判断时间戳时间 --> 150条
        String respBody = huoBiApiClient.kline(symbol, type, 150);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        // 解析
        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                RemoteSyn<List<MyKline>> remoteSyn = new RemoteSyn<>();

                remoteSyn.setTs(apiResp.getTs());

                // myKlineList
                List<MyKline> myKlineList = new LinkedList<>();

                List<Kline> klineList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Kline>>() { });
                Integer count = klineList.size();
                for (int i = 0; i < count; i++) {
                    Kline kline = klineList.get(i);
                    MyKline myKline = new MyKline();
                    myKline.setId(kline.getId());
                    myKline.setOpen(kline.getOpen().toString());
                    myKline.setHigh(kline.getHigh().toString());
                    myKline.setLow(kline.getLow().toString());
                    myKline.setClose(kline.getClose().toString());
                    myKline.setAmount(kline.getAmount().toString());
                    myKlineList.add(myKline);
                }
                remoteSyn.setData(myKlineList);

                // huobi
                //   kline_symbol_type(例: huobi_btcusdt_1min)
                // 存储 redis格式
                // {
                //    Long ts: 1533095400000
                //    Object data: [{}, {}]
                // }

                return remoteSyn;
            }

            throw new KlineSynException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }

    public RemoteSyn getTrades(String symbol) throws Exception {

//        IApiClient apiClient = ApiClinetFactory.getInstence("huobi");

        // 分析数据 100条内 肯定存在 大于 10条的买 和 大于 10条的卖
        String respBody = huoBiApiClient.trades(symbol, 100);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                RemoteSyn<MyTrades> remoteSyn = new RemoteSyn<>();

                remoteSyn.setTs(apiResp.getTs());

                MyTrades myTrades = new MyTrades();

                // myKlineList
                List<MyTrade> tradeList_buy = new LinkedList<>();
                List<MyTrade> tradeList_sell = new LinkedList<>();

                List<Trades> tradesList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Trades>>() { });
                for (Trades trades:tradesList) {
                    List<Trade> tradeList = trades.getData();
                    for (Trade trade:tradeList) {
                        if ("buy".equals(trade.getDirection())) {
                            MyTrade myTrade = new MyTrade();
                            myTrade.setId(trade.getId());
                            myTrade.setTs(trade.getTs());
                            myTrade.setPrice(trade.getPrice());
                            myTrade.setAmount(trade.getAmount());
                            myTrade.setDirection(trade.getDirection());
                            tradeList_buy.add(myTrade);
                        } else if ("sell".equals(trade.getDirection())) {
                            MyTrade myTrade = new MyTrade();
                            myTrade.setId(trade.getId());
                            myTrade.setTs(trade.getTs());
                            myTrade.setPrice(trade.getPrice());
                            myTrade.setAmount(trade.getAmount());
                            myTrade.setDirection(trade.getDirection());
                            tradeList_sell.add(myTrade);
                        }
                    }
                }

                myTrades.setBuy(tradeList_buy);
                myTrades.setSell(tradeList_sell);

                remoteSyn.setData(myTrades);

                return remoteSyn;
            }

            throw new TradesSynException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }

    public Map<String, UserCurrency> accounts(String apiKey, String apiSecret, Integer userId) throws Exception {

//        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);

        Date date = new Date();

        // 获取用户信息，保存数据库
        String respBody = huoBiApiClient.accounts(apiKey, apiSecret);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {
                Map<String, UserCurrency> map = new HashMap<>();

                Account account = objectMapper.convertValue(apiResp.getData(), Account.class);
                List<Balance> balanceList = objectMapper.convertValue(account.getList(), new TypeReference<List<Balance>>() { });
                // 非安全
                for (Balance balance:balanceList) {
                    if (Double.valueOf(balance.getBalance()) > 0) {
                        // 根据币种判断 map 是否含有对象
                        UserCurrency userCurrency = map.get(balance.getCurrency());
                        if (userCurrency == null) {
                            userCurrency = new UserCurrency();
                            userCurrency.setFree("0");
                            userCurrency.setFreezed("0");
                        }
                        userCurrency.setExchangeName("huobi");
                        userCurrency.setCurrency(balance.getCurrency());
                        if ("trade".equals(balance.getType())) {
                            userCurrency.setFree(balance.getBalance());
                        }else {
                            // "frozen"
                            userCurrency.setFreezed(balance.getBalance());
                        }
                        userCurrency.setUserId(userId);
                        userCurrency.setApiKey(apiKey);
                        userCurrency.setApiSecret(apiSecret);
                        userCurrency.setAccountId(account.getId().toString());
                        userCurrency.setCreateTime(date);

                        map.put(balance.getCurrency(), userCurrency);
                    }
                }
                return map;
            }

            throw new TradesSynException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }

    public RemotePost<String> placeOrder(String apiKey, String apiSecret, String accountId, String symbol, String price, String amount, String type) throws Exception {

//        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);

        Date date = new Date();

        String respBody = huoBiApiClient.placeOrder(apiKey, apiSecret, accountId, symbol, price, amount, type);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                RemotePost<String> remotePost = new RemotePost<>();
                remotePost.setStatus(apiResp.getStatus());
                remotePost.setData(apiResp.getData().toString());

                return remotePost;
            }

            throw new PlaceOrderPostException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }

    public RemoteSyn getOpenOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {

//        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);

        Long ts = System.currentTimeMillis();

        // 分析数据 100条内 肯定存在 大于 10条的买 和 大于 10条的卖
        String respBody = huoBiApiClient.openOrders(apiKey, apiSecret, accountId, symbol, 0, 100);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                RemoteSyn<List<MyOpenOrders>> remoteSyn = new RemoteSyn<>();

                remoteSyn.setTs(ts);

                List<MyOpenOrders> myOpenOrdersList = new LinkedList<>();

                List<OpenOrders> openOrdersList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<OpenOrders>>() {});
                for (OpenOrders openOrders:openOrdersList) {

                    if ("buy-market".equals(openOrders.getType()) || "sell-market".equals(openOrders.getType()) ||
                        "buy-limit".equals(openOrders.getType()) || "sell-limit".equals(openOrders.getType())) {
                        MyOpenOrders myOpenOrders = new MyOpenOrders();
                        myOpenOrders.setSource(openOrders.getSource());
                        myOpenOrders.setSymbol(openOrders.getSymbol());
                        myOpenOrders.setAmount(openOrders.getAmount());
                        myOpenOrders.setCreatedAt(openOrders.getCreatedAt());
                        myOpenOrders.setCreatedAtDisplay(new Date(openOrders.getCreatedAt()));
                        myOpenOrders.setPrice(openOrders.getPrice());
                        myOpenOrders.setId(openOrders.getId());

                        myOpenOrders.setState(openOrders.getState());
                        Integer myState = 0;
                        String stateDisplay = "";
                        switch (openOrders.getState()) {
                            case "submitted":
                                stateDisplay = "撤销";
                                break;
                            case "partial-filled":
                                stateDisplay = "已成交";
                                myState = 1;
                                break;
                            case "cancelling":
                                stateDisplay = "正在取消";
                                myState = 1;
                                break;
                        }
                        myOpenOrders.setMyState(myState);
                        myOpenOrders.setStateDisplay(stateDisplay);

                        myOpenOrders.setType(openOrders.getType());
                        Integer myType = 0;
                        String typeDisplay = "";
                        switch (openOrders.getType()) {
                            case "buy-market":
                                typeDisplay = "市价买";
                                break;
                            case "sell-market":
                                typeDisplay = "市价卖";
                                myType = 1;
                                break;
                            case "buy-limit":
                                typeDisplay = "限价买";
                                break;
                            case "sell-limit":
                                typeDisplay = "限价卖";
                                break;
                        }
                        myOpenOrders.setTypeDisplay(typeDisplay);
                        myOpenOrders.setMyType(myType);

                        myOpenOrdersList.add(myOpenOrders);
                    }
                }

                remoteSyn.setData(myOpenOrdersList);

                return remoteSyn;
            }

            throw new TradesSynException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }

    @Override
    public RemoteSyn getHistoryOrders(String apiKey, String apiSecret, String accountId, String symbol, Integer status, Integer size) throws Exception {

//        IApiClient apiClient = new HuoBiApiClient(apiKey, apiSecret);

        Long ts = System.currentTimeMillis();

        // 分析数据 100条内 肯定存在 大于 10条的买 和 大于 10条的卖
        String respBody = huoBiApiClient.historyOrders(apiKey, apiSecret, accountId, symbol, 0, 100);

        if (logger.isDebugEnabled()) {
            logger.debug(respBody);
        }

        if (!StringUtils.isBlank(respBody)) {
            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                RemoteSyn<List<MyHistoryOrders>> remoteSyn = new RemoteSyn<>();

                remoteSyn.setTs(ts);

                List<MyHistoryOrders> myHistoryOrdersList = new LinkedList<>();

                List<HistoryOrders> historyOrdersList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<HistoryOrders>>() {});
                for (HistoryOrders historyOrders:historyOrdersList) {

                    if ("buy-market".equals(historyOrders.getType()) || "sell-market".equals(historyOrders.getType()) ||
                            "buy-limit".equals(historyOrders.getType()) || "sell-limit".equals(historyOrders.getType())) {
                        MyHistoryOrders myHistoryOrders = new MyHistoryOrders();
                        myHistoryOrders.setId(historyOrders.getId());
                        myHistoryOrders.setOrderId(historyOrders.getOrderId());
                        myHistoryOrders.setMatchId(historyOrders.getMatchId());
                        myHistoryOrders.setSymbol(historyOrders.getSymbol());

                        myHistoryOrders.setType(historyOrders.getType());
                        String typeDisplay = "";
                        switch (historyOrders.getType()) {
                            case "buy-market":
                                typeDisplay = "市价买";
                                break;
                            case "sell-market":
                                typeDisplay = "市价卖";
                                break;
                            case "buy-limit":
                                typeDisplay = "限价买";
                                break;
                            case "sell-limit":
                                typeDisplay = "限价卖";
                                break;
                        }
                        myHistoryOrders.setTypeDisplay(typeDisplay);

                        myHistoryOrders.setSource(historyOrders.getSource());
                        myHistoryOrders.setPrice(historyOrders.getPrice());
                        myHistoryOrders.setAmount(historyOrders.getFilledAmount());
                        myHistoryOrders.setFees(historyOrders.getFilledFees());
                        myHistoryOrders.setCreatedAt(historyOrders.getCreatedAt());
                        myHistoryOrders.setCreateAtDisplay(new Date(historyOrders.getCreatedAt()));

                        myHistoryOrdersList.add(myHistoryOrders);
                    }
                }

                remoteSyn.setData(myHistoryOrdersList);

                return remoteSyn;
            }

            throw new TradesSynException(apiResp.getErrMsg());
        }

        throw new NetworkException();
    }


}
