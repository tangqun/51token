package com.sharex.token.api.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.Account;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.currency.huobi.resp.Balance;
import org.junit.Test;

import java.util.List;

public class HuoBiApiClientTest {

    private static final String apiKey_huobi = "2e2fab8c-474a3052-12f899a4-f34d7";
    private static final String apiSecret_huobi = "afbc2bc8-93c0e70a-a1c25d2c-d5869";

    private static final Integer accountId_huobi = 4344135;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void trades() throws Exception {

        IApiClient apiClient = new HuoBiApiClient();

        String respBody = apiClient.trades("btcusdt", 100);

        System.out.println(respBody);
    }

    @Test
    public void ticker() throws Exception {

        IApiClient apiClient = new HuoBiApiClient();

        String respBody = apiClient.ticker("btcusdt");

        System.out.println(respBody);
    }

    @Test
    public void kline() throws Exception {

        IApiClient apiClient = new HuoBiApiClient();

        String respBody = apiClient.kline("btcusdt", "1min", 150);

        System.out.println(respBody);
    }

    @Test
    public void accounts() throws Exception {

        IApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.accounts();

        System.out.println(respBody);

        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);

        if ("ok".equals(apiResp.getStatus())) {

            Account account = objectMapper.convertValue(apiResp.getData(), Account.class);

            List<Balance> balanceList = account.getList();

            for (Balance balance: balanceList) {

                System.out.println("currency: " + balance.getCurrency() + ", type: " + balance.getType() + ", balance: " + balance.getBalance());
            }
        }
    }

    @Test
    public void historyOrders() throws Exception {

        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.historyOrders("btcusdt", 0);

        System.out.println(respBody);
    }

    @Test
    public void entrustOrders() throws Exception {

        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.entrustOrders("btcusdt");

        System.out.println(respBody);
    }

    @Test
    public void placeOrder() throws Exception {


        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.placeOrder(accountId_huobi.toString(), "btcusdt", "0.1", "0.02", "buy-limit");

        System.out.println(respBody);
    }
}
