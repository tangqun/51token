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

    // 交易所名称
    private String exchangeName;

    // 币种
    private String currency;

    // trade: 交易余额，frozen: 冻结余额
    private String type;

    // 金额（数量）
    private String balance;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
