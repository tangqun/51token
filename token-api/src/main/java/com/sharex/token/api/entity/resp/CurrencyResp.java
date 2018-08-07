package com.sharex.token.api.entity.resp;

public class CurrencyResp {

    // 总资产
    private String vol;

    // 累计收益
    private String cumulativeProfit;

    // 累计收益率
    private String cumulativeProfitRate;

    // 成本
    private String cost;

    // 今日收益
    private String profit;

    // 今日收益率
    private String profitRate;

    // 币种
    private String currency;

    // 账户余额
    private String free;

    // 冻结余额
    private String freezed;

    // 现价 行情里的 close
    private String price;

    public String getVol() {
        return vol;
    }

    public void setVol(String vol) {
        this.vol = vol;
    }

    public String getCumulativeProfit() {
        return cumulativeProfit;
    }

    public void setCumulativeProfit(String cumulativeProfit) {
        this.cumulativeProfit = cumulativeProfit;
    }

    public String getCumulativeProfitRate() {
        return cumulativeProfitRate;
    }

    public void setCumulativeProfitRate(String cumulativeProfitRate) {
        this.cumulativeProfitRate = cumulativeProfitRate;
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

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getFreezed() {
        return freezed;
    }

    public void setFreezed(String freezed) {
        this.freezed = freezed;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
