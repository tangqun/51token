package com.sharex.token.api.entity.resp;

import java.util.List;

public class AssetResp {

    // 总资产
    private String vol;

    // 今日收益
    private String profit;

    // 累计收益
    private String cumulativeProfit;

    // 收益率
    private String profitRate;

    private List<UserExchangeAssetResp> userExchangeAssetRespList;

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
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

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public List<UserExchangeAssetResp> getUserExchangeAssetRespList() {
        return userExchangeAssetRespList;
    }

    public void setUserExchangeAssetRespList(List<UserExchangeAssetResp> userExchangeAssetRespList) {
        this.userExchangeAssetRespList = userExchangeAssetRespList;
    }
}
