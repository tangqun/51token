package com.sharex.token.api.entity.resp;

import java.util.List;

public class UserExchangeAssetResp {

    private String name;

    // 单交易所币种个数
    private Integer currencyCount;

    // 市值--新增20180808
    private String vol;

    // 成本--新增20180808
    private String cost;

    // 单交易所今日收益
    private String profit;

    // 单交易所累计收益
    private String cumulativeProfit;

    private List<UserCurrencyAssetResp> userCurrencyAssetRespList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCurrencyCount() {
        return currencyCount;
    }

    public void setCurrencyCount(Integer currencyCount) {
        this.currencyCount = currencyCount;
    }

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public List<UserCurrencyAssetResp> getUserCurrencyAssetRespList() {
        return userCurrencyAssetRespList;
    }

    public void setUserCurrencyAssetRespList(List<UserCurrencyAssetResp> userCurrencyAssetRespList) {
        this.userCurrencyAssetRespList = userCurrencyAssetRespList;
    }

    public String getCumulativeProfit() {
        return cumulativeProfit;
    }

    public void setCumulativeProfit(String cumulativeProfit) {
        this.cumulativeProfit = cumulativeProfit;
    }
}
