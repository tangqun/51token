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

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.accounts(apiKey_okex, apiSecret_okex);

        System.out.println(respBody);
    }

    @Test
    public void openOrders() throws Exception {

        OkexApiClient okexApiClient = new OkexApiClient();

        String respBody = okexApiClient.openOrders(apiKey_okex, apiSecret_okex, null, "eth_usdt", 0, 100);

        System.out.println(respBody);
    }

    @Test
    public void historyOrders() throws Exception {

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.historyOrders(apiKey_okex, apiSecret_okex, null,"eth_usdt", 0, 100);

        System.out.println(respBody);
    }

    @Test
    public void placeOrder() throws Exception {

        IApiClient apiClient = new OkexApiClient();

        String respBody = apiClient.placeOrder(apiKey_okex, apiSecret_okex, null, "eth_usdt", "422.9", "0.001", "sell");

        System.out.println(respBody);
    }
}
