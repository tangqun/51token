package com.sharex.token.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sharex.token.api.concurrent.HuoBiKlineThread;
import com.sharex.token.api.currency.IApiClient;
import com.sharex.token.api.currency.huobi.HuoBiApiClient;
import com.sharex.token.api.currency.huobi.resp.ApiResp;
import com.sharex.token.api.currency.huobi.resp.Trade;
import com.sharex.token.api.currency.huobi.resp.Trades;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Component
public class ScheduleService {

    private static final Log logger = LogFactory.getLog(ScheduleService.class);

    @Autowired
    private ValueOperations valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private RedisTemplate redisTemplate;

    // huobi
    private IApiClient apiClient = new HuoBiApiClient();

    private ObjectMapper objectMapper = new ObjectMapper();

//    @Scheduled(cron="0/10 * * * * ?")
    public void synKline() {

        // 1min, 15min, 30min, 1hour, 1day, 1week

        // huobi
        synKline_HuoBi();

        // okex

        // binance
    }

    private void synKline_HuoBi() {
        try {
            // 要求数据格式转成统一的 包含 字段个数、字段名
            String[] symbols = {
                    "btcusdt", "bchusdt", "ethusdt", "etcusdt", "ltcusdt", "eosusdt",
                    "xrpusdt", "omgusdt", "dashusdt", "zecusdt", "adausdt", "steemusdt",
                    "iotausdt", "ocnusdt", "socusdt", "ctxcusdt", "actusdt", "btmusdt",
                    "btsusdt", "ontusdt", "iostusdt", "htusdt", "trxusdt", "dtausdt",
                    "neousdt", "qtumusdt", "smtusdt", "elausdt", "venusdt", "thetausdt",
                    "sntusdt", "zilusdt", "xemusdt", "nasusdt", "ruffusdt", "hsrusdt",
                    "letusdt", "mdsusdt", "storjusdt", "elfusdt", "itcusdt", "cvcusdt",
                    "gntusdt", "xmrbtc", "bchbtc", "ethbtc", "ltcbtc", "etcbtc",
                    "eosbtc", "omgbtc", "xrpbtc", "dashbtc", "zecbtc", "adabtc",
                    "steembtc", "iotabtc", "polybtc", "kanbtc", "lbabtc", "wanbtc",
                    "bftbtc", "btmbtc", "ontbtc", "iostbtc", "htbtc", "trxbtc",
                    "smtbtc", "elabtc", "wiccbtc", "ocnbtc", "zlabtc", "abtbtc",
                    "mtxbtc", "nasbtc", "venbtc", "dtabtc", "neobtc", "waxbtc",
                    "btsbtc", "zilbtc", "thetabtc", "ctxcbtc", "srnbtc", "xembtc",
                    "icxbtc", "dgdbtc", "chatbtc", "wprbtc", "lunbtc", "swftcbtc",
                    "sntbtc", "meetbtc", "yeebtc", "elfbtc", "letbtc", "qtumbtc",
                    "lskbtc", "itcbtc", "socbtc", "qashbtc", "mdsbtc", "ekobtc",
                    "topcbtc", "mtnbtc", "actbtc", "hsrbtc", "stkbtc", "storjbtc",
                    "gnxbtc", "dbcbtc", "sncbtc", "cmtbtc", "tnbbtc", "ruffbtc",
                    "qunbtc", "zrxbtc", "kncbtc", "blzbtc", "propybtc", "rpxbtc",
                    "appcbtc", "aidocbtc", "powrbtc", "cvcbtc", "paybtc", "qspbtc",
                    "datbtc", "rdnbtc", "mcobtc", "rcnbtc", "manabtc", "utkbtc",
                    "tntbtc", "gasbtc", "batbtc", "ostbtc", "linkbtc", "gntbtc",
                    "mtlbtc", "evxbtc", "reqbtc", "adxbtc", "astbtc", "engbtc",
                    "saltbtc", "edubtc", "wtcbtc", "bifibtc", "bcxbtc", "bcdbtc",
                    "sbtcbtc", "btgbtc", "xmreth", "eoseth", "omgeth", "iotaeth",
                    "adaeth", "steemeth", "polyeth", "kaneth", "lbaeth", "waneth",
                    "bfteth", "zrxeth", "asteth", "knceth", "onteth", "hteth",
                    "btmeth", "iosteth", "smteth", "elaeth", "trxeth", "abteth",
                    "naseth", "ocneth", "wicceth", "zileth", "ctxceth", "zlaeth",
                    "wpreth", "dtaeth", "mtxeth", "thetaeth", "srneth", "veneth",
                    "btseth", "waxeth", "hsreth", "icxeth", "mtneth", "acteth",
                    "blzeth", "qasheth", "ruffeth", "cmteth", "elfeth", "meeteth",
                    "soceth", "qtumeth", "itceth", "swftceth", "yeeeth", "lsketh",
                    "luneth", "leteth", "gnxeth", "chateth", "ekoeth", "topceth",
                    "dgdeth", "stketh", "mdseth", "dbceth", "snceth", "payeth",
                    "quneth", "aidoceth", "tnbeth", "appceth", "rdneth", "utketh",
                    "powreth", "bateth", "propyeth", "manaeth", "reqeth", "cvceth",
                    "qspeth", "evxeth", "dateth", "mcoeth", "gnteth", "gaseth",
                    "osteth", "linketh", "rcneth", "tnteth", "engeth", "salteth",
                    "adxeth", "edueth", "wtceth", "xrpht", "iostht", "dashht",
                    "wiccusdt", "eosht", "bchht", "ltcht", "etcht", "wavesbtc",
                    "waveseth", "hb10usdt", "cmtusdt", "dcrbtc", "dcreth", "paibtc",
                    "paieth", "boxbtc", "boxeth", "dgbbtc", "dgbeth", "gxsbtc",
                    "gxseth", "xlmbtc", "xlmeth", "bixbtc", "bixeth", "bixusdt"

            };
//            String[] types = {"1min", "15min", "30min", "60min", "1day", "1week"};

            ExecutorService executorService = Executors.newFixedThreadPool(1);

            for (int i = 0; i < symbols.length; i++) {
                String symbol = symbols[i];

                executorService.execute(new HuoBiKlineThread(symbol));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 最新成交
     */
//    @Scheduled(cron="0/10 * * * * ?")
    public void synTrade() {

        // huobi

        // okex

        // binance
    }

    private void synTrade_HuoBi() {
        try {

            String respBody = apiClient.trades("btcusdt", 100);

            // {"status":"ok","ch":"market.btcusdt.trade.detail","ts":1532594863670,"data":[
            // {"id":13791167722,"ts":1532594861775,"data":[{"amount":0.012200000000000000,"ts":1532594861775,"id":137911677228650540740,"price":8258.820000000000000000,"direction":"buy"}]},
            // {"id":13791166720,"ts":1532594860908,"data":[{"amount":0.939100000000000000,"ts":1532594860908,"id":137911667208650540740,"price":8258.820000000000000000,"direction":"buy"}]}]}
            System.out.println(respBody);

            if (!StringUtils.isBlank(respBody)) {
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
                    //   huobi_symbol_buy
                    //   huobi_symbol_sell
                    hashOperations.put("trades", "huobi_btcusdt_buy", objectMapper.writeValueAsString(tradeList_buy));
                    hashOperations.put("trades", "huobi_btcusdt_sell", objectMapper.writeValueAsString(tradeList_sell));
                }
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }
}
