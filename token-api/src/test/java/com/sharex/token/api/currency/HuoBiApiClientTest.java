package com.sharex.token.api.currency;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.Accounts;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import org.junit.Test;

import java.util.List;

public class HuoBiApiClientTest {

    private static final String apiKey_huobi = "d2ccc964-813249e6-595ee6d8-574fb";
    private static final String apiSecret_huobi = "bde5a51b-63d25997-a83a30c2-789ae";

    private static final Integer accountId_huobi = 4344135;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void ticker() throws Exception {

        IApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.ticker("btcusdt");

        System.out.println(respBody);
    }

    @Test
    public void kline() throws Exception {

        IApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.kline("btcusdt", "1min", 0);

        System.out.println(respBody);
    }

    @Test
    public void accounts() throws Exception {

        IApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.accounts();

        System.out.println(respBody);

        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);

        if ("ok".equals(apiResp.getStatus())) {

            List<Accounts> accountsList = objectMapper.convertValue(apiResp.getData(), new TypeReference<List<Accounts>>() { });

            for (Accounts accounts: accountsList) {

                System.out.println(accounts.getType());
            }
        }

        // 错误消息直接 RESTful -> msg
    }

    @Test
    public void account() throws Exception {

//        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);
//
//        String respBody = apiClient.account(accountId_huobi);
//
//        System.out.println(respBody);
//
//        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);
//
//        if ("ok".equals(apiResp.getStatus())) {
//
////            Account account = objectMapper.convertValue(apiResp.getData(), Account.class);
//
//            Account account = objectMapper.convertValue(apiResp.getData(), new TypeReference<Account>() { });
//
//            List<Balance> balanceList = account.getList();
//
//            for (Balance balance: balanceList) {
//
//                System.out.println(balance.getCurrency());
//            }
//        }
    }

    @Test
    public void entrustOrders() throws Exception {

        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.entrustOrders("btcusdt");

        System.out.println(respBody);

//        ApiResp apiResp = objectMapper.readValue(respBody, ApiResp.class);

//        if ("ok".equals(apiResp.getStatus())) {
//
//
//        }
    }

    @Test
    public void placeOrder() throws Exception {


        HuoBiApiClient apiClient = new HuoBiApiClient(apiKey_huobi, apiSecret_huobi);

        String respBody = apiClient.placeOrder(accountId_huobi.toString(), "btcusdt", "0.1", "0.02", "buy-limit");

        System.out.println(respBody);
    }
}
