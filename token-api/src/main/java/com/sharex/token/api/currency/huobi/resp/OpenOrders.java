package com.sharex.token.api.currency.huobi.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class OpenOrders {

    private long id;
    private String symbol;
    @JsonProperty("account-id")
    private int accountId;
    private String amount;
    private String price;
    @JsonProperty("created-at")
    private long createdAt;
    private String type;
    @JsonProperty("filled-amount")
    private String filledAmount;
    @JsonProperty("filled-cash-amount")
    private String filledCashAmount;
    @JsonProperty("filled-fees")
    private String filledFees;
    private String source;
    private String state;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFilledAmount() {
        return filledAmount;
    }

    public void setFilledAmount(String filledAmount) {
        this.filledAmount = filledAmount;
    }

    public String getFilledCashAmount() {
        return filledCashAmount;
    }

    public void setFilledCashAmount(String filledCashAmount) {
        this.filledCashAmount = filledCashAmount;
    }

    public String getFilledFees() {
        return filledFees;
    }

    public void setFilledFees(String filledFees) {
        this.filledFees = filledFees;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
