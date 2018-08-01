package com.sharex.token.api.concurrent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.currency.huobi.resp.Kline;
import com.sharex.token.api.entity.MyKline;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;

import java.util.LinkedList;
import java.util.List;

public class HuoBiKlineThread extends Thread {

    private static final String[] types = {"1min", "15min", "30min", "60min", "1day", "1week"};

    private String symbol;
    private IApiClient apiClient = new HuoBiApiClient();

    public HuoBiKlineThread(String symbol) {
        this.symbol = symbol;
    }

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void run() {
        try {
            for (int j = 0; j < types.length; j++) {
                String type = types[j];
                String respBody = apiClient.kline(symbol, type,150);

                System.out.println(Thread.currentThread().getName() + ", ......" + respBody);

                if (!StringUtils.isBlank(respBody)) {
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

                                // ticker
                                //   huobi_symbol_lastest
//                                hashOperations.put("ticker", "huobi_"+ symbol +"_lastest", objectMapper.writeValueAsString(myKline));
                            }

                            myKlineList.add(myKline);
                        }

                        // kline
                        //   huobi_symbol_type(例: huobi_btcusdt_1min)
//                        hashOperations.put("kline", "huobi_" + symbol + "_" + type, objectMapper.writeValueAsString(myKlineList));
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
