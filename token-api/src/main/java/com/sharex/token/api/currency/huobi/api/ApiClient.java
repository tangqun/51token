package com.sharex.token.api.currency.huobi.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.currency.huobi.request.CreateOrderRequest;
import com.sharex.token.api.currency.huobi.request.DepthRequest;
import com.sharex.token.api.currency.huobi.request.IntrustOrdersDetailRequest;
import com.sharex.token.api.currency.huobi.response.*;
import com.sharex.token.api.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ApiClient {

    private static final String API_HOST = "api.huobipro.com";
    private static final String API_URL = "https://" + API_HOST;

    final String accessKeyId;
    final String accessKeySecret;
    final String assetPassword;

    /**
     * 创建一个ApiClient实例
     *
     * @param accessKeyId     AccessKeyId
     * @param accessKeySecret AccessKeySecret
     */
    public ApiClient(String accessKeyId, String accessKeySecret) {
        this.accessKeyId = accessKeyId;
        this.accessKeySecret = accessKeySecret;
        this.assetPassword = null;
    }

    /**
     * 查询交易对
     *
     * @return List of symbols.
     */
    public List<Symbol> getSymbols() {
        ApiResponse<List<Symbol>> resp =
                get("/v1/common/symbols", null, new TypeReference<ApiResponse<List<Symbol>>>() {
                });
        return resp.checkAndReturn();
    }

    /**
     * 查询所有账户信息
     *
     * @return List of accounts.
     */
    public List<Account> getAccounts() {
        ApiResponse<List<Account>> resp =
                get("/v1/account/accounts", null, new TypeReference<ApiResponse<List<Account>>>() {
                });
        return resp.checkAndReturn();
    }

    /**
     * 创建订单（未执行)
     *
     * @param request CreateOrderRequest object.
     * @return Order id.
     */
    public Long createOrder(CreateOrderRequest request) {
        ApiResponse<Long> resp =
                post("/v1/order/orders", request, new TypeReference<ApiResponse<Long>>() {
                });
        return resp.checkAndReturn();
    }

    /**
     * 执行订单
     *
     * @param orderId The id of created order.
     * @return Order id.
     */
    public String placeOrder(long orderId) {
        ApiResponse<String> resp = post("/v1/order/orders/" + orderId + "/place", null,
                new TypeReference<ApiResponse<String>>() {
                });
        return resp.checkAndReturn();
    }


    // ----------------------------------------行情API-------------------------------------------

    /**
     * GET /market/history/kline 获取K线数据
     *
     * @param symbol
     * @param period
     * @param size
     * @return
     */
    public KlineResponse kline(String symbol, String period, String size) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        map.put("period", period);
        map.put("size", size);
        KlineResponse resp = get("/market/history/kline", map, new TypeReference<KlineResponse<List<Kline>>>() {
        });
        return resp;
    }

    /**
     * GET /market/detail/merged 获取聚合行情(Ticker)
     *
     * @param symbol
     * @return
     */
    public MergedResponse merged(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        MergedResponse resp = get("/market/detail/merged", map, new TypeReference<MergedResponse<List<Merged>>>() {
        });
        return resp;
    }

    /**
     * GET /market/depth 获取 Market Depth 数据
     *
     * @param request
     * @return
     */
    public DepthResponse depth(DepthRequest request) {
        HashMap map = new HashMap();
        map.put("symbol", request.getSymbol());
        map.put("type", request.getType());

        DepthResponse resp = get("/market/depth", map, new TypeReference<DepthResponse<List<Depth>>>() {
        });
        return resp;
    }

    /**
     * GET /market/trade 获取 Trade Detail 数据
     *
     * @param symbol
     * @return
     */
    public TradeResponse trade(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        TradeResponse resp = get("/market/trade", map, new TypeReference<TradeResponse>() {
        });
        return resp;
    }

    /**
     * GET /market/history/trade 批量获取最近的交易记录
     *
     * @param symbol
     * @param size
     * @return
     */
    public HistoryTradeResponse historyTrade(String symbol, String size) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        map.put("size", size);
        HistoryTradeResponse resp = get("/market/history/trade", map, new TypeReference<HistoryTradeResponse>() {
        });
        return resp;
    }

    /**
     * GET /market/detail 获取 Market Detail 24小时成交量数据
     *
     * @param symbol
     * @return
     */
    public DetailResponse detail(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        DetailResponse resp = get("/market/detail", map, new TypeReference<DetailResponse<Details>>() {
        });
        return resp;
    }


    /**
     * GET /v1/common/symbols 查询系统支持的所有交易对及精度
     *
     * @param symbol
     * @return
     */
    public SymbolsResponse symbols(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        SymbolsResponse resp = get("/v1/common/symbols", map, new TypeReference<SymbolsResponse<Symbols>>() {
        });
        return resp;
    }

    /**
     * GET /v1/common/currencys 查询系统支持的所有币种
     *
     * @param symbol
     * @return
     */
    public CurrencysResponse currencys(String symbol) {
        HashMap map = new HashMap();
        map.put("symbol", symbol);
        CurrencysResponse resp = get("/v1/common/currencys", map, new TypeReference<CurrencysResponse>() {
        });
        return resp;
    }

    /**
     * GET /v1/common/timestamp 查询系统当前时间
     *
     * @return
     */
    public TimestampResponse timestamp() {
        TimestampResponse resp = get("/v1/common/timestamp", null, new TypeReference<TimestampResponse>() {
        });
        return resp;
    }

    /**
     * GET /v1/account/accounts 查询当前用户的所有账户(即account-id)
     *
     * @return
     */
    public AccountsResponse accounts() {
        AccountsResponse resp = get("/v1/account/accounts", null, new TypeReference<AccountsResponse<List<Accounts>>>() {
        });
        return resp;
    }

    /**
     * GET /v1/account/accounts/{account-id}/balance 查询指定账户的余额
     *
     * @param accountId
     * @return
     */
    public BalanceResponse balance(String accountId) {
        BalanceResponse resp = get("/v1/account/accounts/" + accountId + "/balance", null, new TypeReference<BalanceResponse<Balance>>() {
        });
        return resp;
    }

    /**
     * POST /v1/order/orders/{order-id}/submitcancel 申请撤销一个订单请求
     *
     * @param orderId
     * @return
     */
    public SubmitcancelResponse submitcancel(String orderId) {
        SubmitcancelResponse resp = post("/v1/order/orders/" + orderId + "/submitcancel", null, new TypeReference<SubmitcancelResponse>() {
        });
        return resp;
    }

    /**
     * POST /v1/order/orders/batchcancel 批量撤销订单
     *
     * @param orderList
     * @return
     */
    public BatchcancelResponse submitcancels(List orderList) {
        Map<String, List> parameterMap = new HashMap();
        parameterMap.put("order-ids", orderList);
        BatchcancelResponse resp = post("/v1/order/orders/batchcancel", parameterMap, new TypeReference<BatchcancelResponse<Batchcancel<List, List<BatchcancelBean>>>>() {
        });
        return resp;
    }

    /**
     * GET /v1/order/orders/{order-id} 查询某个订单详情
     *
     * @param orderId
     * @return
     */
    public OrdersDetailResponse ordersDetail(String orderId) {
        OrdersDetailResponse resp = get("/v1/order/orders/" + orderId, null, new TypeReference<OrdersDetailResponse>() {
        });
        return resp;
    }


    /**
     * GET /v1/order/orders/{order-id}/matchresults 查询某个订单的成交明细
     *
     * @param orderId
     * @return
     */
    public MatchresultsOrdersDetailResponse matchresults(String orderId) {
        MatchresultsOrdersDetailResponse resp = get("/v1/order/orders/" + orderId + "/matchresults", null, new TypeReference<MatchresultsOrdersDetailResponse>() {
        });
        return resp;
    }

    /**
     * Get /v1/order/orders 查询当前委托、历史委托
     * @param req
     * @return
     */
    public IntrustDetailResponse intrustOrdersDetail(IntrustOrdersDetailRequest req) {
        HashMap map = new HashMap();
        map.put("symbol", req.symbol);
        map.put("states", req.states);
        if (req.startDate!=null) {
            map.put("startDate",req.startDate);
        }
        if (req.startDate!=null) {
            map.put("start-date",req.startDate);
        }
        if (req.endDate!=null) {
            map.put("end-date",req.endDate);
        }
        if (req.types!=null) {
            map.put("types",req.types);
        }
        if (req.from!=null) {
            map.put("from",req.from);
        }
        if (req.direct!=null) {
            map.put("direct",req.direct);
        }
        if (req.size!=null) {
            map.put("size",req.size);
        }

        IntrustDetailResponse resp = get("/v1/order/orders/", map, new TypeReference<IntrustDetailResponse<List<IntrustDetail>>>() {
        });
        return resp;
    }

    // send a GET request.
    <T> T get(String uri, Map<String, String> params, TypeReference<T> ref) {
        if (params == null) {
            params = new HashMap<>();
        }
        return call("GET", uri, null, params, ref);
    }

    // send a POST request.
    <T> T post(String uri, Object object, TypeReference<T> ref) {
        return call("POST", uri, object, new HashMap<String, String>(), ref);
    }

    // call api by endpoint.
    <T> T call(String method, String uri, Object object, Map<String, String> params,
               TypeReference<T> ref) {
        ApiSignature sign = new ApiSignature();
        sign.createSignature(this.accessKeyId, this.accessKeySecret, method, API_HOST, uri, params);
        try {
//            Request.Builder builder = null;
//            if ("POST".equals(method)) {
//                RequestBody body = RequestBody.create(JSON, JsonUtil.writeValue(object));
//                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).post(body);
//            } else {
//                builder = new Request.Builder().url(API_URL + uri + "?" + toQueryString(params)).get();
//            }
//            if (this.assetPassword != null) {
//                builder.addHeader("AuthData", authData());
//            }
//            Request request = builder.build();
//            Response response = client.newCall(request).execute();
//            String s = response.body().string();
//            return JsonUtil.readValue(s, ref);

            ObjectMapper objectMapper = new ObjectMapper();

            String resp = null;
            StringBuffer sb = new StringBuffer();
            if ("POST".equals(method)) {
                resp = HttpUtil.post(API_URL + uri + "?" + toQueryString(params), objectMapper.writeValueAsString(object));
            } else {
                resp = HttpUtil.get(API_URL + uri + "?" + toQueryString(params));
            }

            return objectMapper.readValue(resp, ref);

        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    // Encode as "a=1&b=%20&c=&d=AAA"
    String toQueryString(Map<String, String> params) {
        return String.join("&", params.entrySet().stream().map((entry) -> {
            return entry.getKey() + "=" + ApiSignature.urlEncode(entry.getValue());
        }).collect(Collectors.toList()));
    }
}
