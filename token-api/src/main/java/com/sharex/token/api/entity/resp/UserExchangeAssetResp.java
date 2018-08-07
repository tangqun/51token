package com.sharex.token.api.entity.resp;

import java.util.List;

public class UserExchangeAssetResp {

    private String name;
    // 币种
    private Integer currencyCount;

    // 今日收益
    private String profit;

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

    public List<UserCurrencyAssetResp> getUserCurrencyAssetRespList() {
        return userCurrencyAssetRespList;
    }

    public void setUserCurrencyAssetRespList(List<UserCurrencyAssetResp> userCurrencyAssetRespList) {
        this.userCurrencyAssetRespList = userCurrencyAssetRespList;
    }
}
