package com.sharex.token.api.entity.resp;

import java.util.List;

public class UserExchangeAssetResp {

    private String name;
    // 币种
    private Integer currencyCount;

    // 今日收益
    private String profit;

    // 累计收益
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

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getCumulativeProfit() {
        return cumulativeProfit;
    }

    public void setCumulativeProfit(String cumulativeProfit) {
        this.cumulativeProfit = cumulativeProfit;
    }

    public List<UserCurrencyAssetResp> getUserCurrencyAssetRespList() {
        return userCurrencyAssetRespList;
    }

    public void setUserCurrencyAssetRespList(List<UserCurrencyAssetResp> userCurrencyAssetRespList) {
        this.userCurrencyAssetRespList = userCurrencyAssetRespList;
    }
}
