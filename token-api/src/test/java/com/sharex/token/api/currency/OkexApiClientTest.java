package com.sharex.token.api.currency;

import com.sharex.token.api.currency.okex.OkexApiClient;
import org.junit.Test;

public class OkexApiClientTest {

    private static final String apiKey_okex = "90bacbb3-bad4-45fc-a4b4-c9e859f97718";
    private static final String apiSecret_okex = "0C459ED2589DB5A00A4E26E3645716DA";

    @Test
    public void trades() throws Exception {

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.trades("btc_usdt", null);

        System.out.println(respBody);
    }

    @Test
    public void ticker() throws Exception {

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.ticker("btc_usdt");

        System.out.println(respBody);
    }

    @Test
    public void kline() throws Exception {

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.kline("btc_usdt", "1min", 150);

        System.out.println(respBody);
    }

    @Test
    public void accounts() throws Exception {

        IApiClient apiClient = new OkexApiClient(apiKey_okex, apiSecret_okex);

        String respBody = apiClient.accounts();

        System.out.println(respBody);
    }

    @Test
    public void historyOrders() throws Exception {

        IApiClient apiClient = new OkexApiClient(apiKey_okex, apiSecret_okex);

        String respBody = apiClient.historyOrders("btc_usdt", 1);

        System.out.println(respBody);
    }

    @Test
    public void placeOrder() throws Exception {

        IApiClient apiClient = new OkexApiClient(apiKey_okex, apiSecret_okex);

        String respBody = apiClient.placeOrder(null, "ltc_btc", "1000", null, "buy_market");

        System.out.println(respBody);
    }
}
