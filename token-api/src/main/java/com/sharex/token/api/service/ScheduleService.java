package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.currency.huobi.resp.Kline;
import com.sharex.token.api.currency.huobi.resp.Trade;
import com.sharex.token.api.currency.huobi.resp.Trades;
import com.sharex.token.api.entity.MyKline;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

@Component
public class ScheduleService {

    private static final Log logger = LogFactory.getLog(ScheduleService.class);

    @Autowired
    private ValueOperations valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    // 需要配置一个固定的 apiKey apiSecret
    private static final String apiKey_huobi = "d2ccc964-813249e6-595ee6d8-574fb";
    private static final String apiSecret_huobi = "bde5a51b-63d25997-a83a30c2-789ae";
    private IApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

    private ObjectMapper objectMapper = new ObjectMapper();

    @Scheduled(cron="0/10 * * * * ?")
    public void synKline() {

        // huobi
        // 多线程执行
        try {
            // 要求数据格式转成统一的 包含 字段个数、字段名
            String[] symbols = {"btcusdt", "etcusdt"};
            String[] types = {"1min", "5min", "15min", "30min", "60min", "1day", "1mon", "1week", "1year"};
            for (int i = 0; i < symbols.length; i++) {
                String symbol = symbols[i];
                for (int j = 0; j < types.length; j++) {
                    String type = types[j];
                    String respBody = apiClient.kline(symbol, type,150);

                    System.out.println(respBody);

                    ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
                    if ("ok".equals(apiResp.getStatus())) {

                        // 最终返回
                        List<MyKline> myKlineList = new LinkedList<>();

                        List<Kline> klineList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Kline>>() { });
                        Integer size = klineList.size();
                        for (int m = 0; m < size; m++) {

                            Kline kline = klineList.get(m);
                            MyKline myKline = new MyKline();
                            myKline.setId(kline.getId());
                            myKline.setOpen(kline.getOpen().toString());
                            myKline.setHigh(kline.getHigh().toString());
                            myKline.setLow(kline.getLow().toString());
                            myKline.setClose(kline.getClose().toString());
                            myKline.setAmount(kline.getAmount().toString());

                            // 最新价保存
                            if ("1min".equals(type) && m == 0) {

                                // 最新价数据保存格式 ticker, huobi_symbol_lastest, mykline
                                hashOperations.put("ticker", "huobi_"+ symbol +"_lastest", objectMapper.writeValueAsString(myKline));
                            }

                            myKlineList.add(myKline);
                        }

                        // k线数据保存格式 kline, huobi_symbol_type(例: huobi_btcusdt_1min), myKlineList
                        hashOperations.put("kline", "huobi_" + symbol + "_" + type, objectMapper.writeValueAsString(myKlineList));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


        // okex

        // binance
    }

    /**
     * 最新成交
     */
    @Scheduled(cron="0/10 * * * * ?")
    public void synTrade() {

        // huobi
        try {

            String respBody = apiClient.trades("btcusdt", 100);

            // {"status":"ok","ch":"market.btcusdt.trade.detail","ts":1532594863670,"data":[
            // {"id":13791167722,"ts":1532594861775,"data":[{"amount":0.012200000000000000,"ts":1532594861775,"id":137911677228650540740,"price":8258.820000000000000000,"direction":"buy"}]},
            // {"id":13791166720,"ts":1532594860908,"data":[{"amount":0.939100000000000000,"ts":1532594860908,"id":137911667208650540740,"price":8258.820000000000000000,"direction":"buy"}]}]}
            System.out.println(respBody);

            ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
            if ("ok".equals(apiResp.getStatus())) {

                List<Trade> tradeList_buy = new LinkedList<>();
                List<Trade> tradeList_sell = new LinkedList<>();

                List<Trades> tradesList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Trades>>() { });
                for (Trades trades:tradesList) {
                    List<Trade> tradeList = trades.getData();
                    for (Trade trade:tradeList) {
                        if ("buy".equals(trade.getDirection())) {

                            tradeList_buy.add(trade);

                        } else if ("sell".equals(trade.getDirection())) {

                            tradeList_sell.add(trade);

                        }
                    }
                }

                // trades
                // huobi_symbol_buy
                // huobi_symbol_sell
                hashOperations.put("trades", "huobi_btcusdt_buy", objectMapper.writeValueAsString(tradeList_buy));
                hashOperations.put("trades", "huobi_btcusdt_sell", objectMapper.writeValueAsString(tradeList_sell));
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }


        // okex

        // binance
    }
}
