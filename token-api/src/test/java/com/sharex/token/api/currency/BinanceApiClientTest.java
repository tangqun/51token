package com.sharex.token.api.currency;

import com.sharex.token.api.currency.binance.BinanceApiClient;
import org.junit.Test;

public class BinanceApiClientTest {

    private static final String apiKey = "psVXNGVss5FTWKTMHJvK54pucvSw96pUuLqaBG4fHDS55Yd1EvIXuRidoAMvIiuY";
    private static final String apiSecret = "aqt1qNrpU79QFZ9bqXm6jSqpw6FEWEaplDojD8ticlxEqy3Qk2plVZw2IiNuqjWd";

    @Test
    public void trades() throws Exception {

        IApiClient apiClient = new BinanceApiClient();

        // 币安 symbol 都是 大写
        String respBody = apiClient.trades("BTCUSDT", 100);

        System.out.println(respBody);
    }

    @Test
    public void ticker() throws Exception {

        IApiClient apiClient = new BinanceApiClient();

        // 币安 symbol 都是 大写
        String respBody = apiClient.ticker("BTCUSDT");

        System.out.println(respBody);
    }

    @Test
    public void kline() throws Exception {

        IApiClient apiClient = new BinanceApiClient();

        // 币安 symbol 都是 大写
        String respBody = apiClient.kline("BTCUSDT", "1m", 150);

        System.out.println(respBody);
    }

    @Test
    public void accounts() throws Exception {

        IApiClient apiClient = new BinanceApiClient();

        String respBody = apiClient.accounts(apiKey, apiSecret);

        System.out.println(respBody);
    }

    @Test
    public void historyOrders() throws Exception {

        IApiClient apiClient = new BinanceApiClient();

        String respBody = apiClient.historyOrders(apiKey, apiSecret, null,"BTCUSDT", 0, 100);

        System.out.println(respBody);
    }
}
