package com.sharex.token.api.entity.resp;

public class UserCurrencyAssetResp {

    // 交易所名称
    private String exchangeName;
    // 币种
    private String currency;
    // trade: 交易余额，frozen: 冻结余额
    private String free;
    // 金额（数量）
    private String freezed;

    // 现价 行情里的 close
    private String price;

    // 市值
    private String vol;

    // 成本
    private String cost;

    // 今日盈亏（单币）
    private String profit;

    // 今日盈亏率（单币）
    private String profitRate;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
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

    public String getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(String profitRate) {
        this.profitRate = profitRate;
    }
}
