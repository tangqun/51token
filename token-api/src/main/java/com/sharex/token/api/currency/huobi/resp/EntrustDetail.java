package com.sharex.token.api.currency.huobi.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EntrustDetail {

    private Long id;
    private String symbol;
    @JsonProperty("account-id")
    private Integer accountId;
    private String amount;
    private String price;
    @JsonProperty("created-at")
    private long createdAt;
    private String type;
    @JsonProperty("field-amount")
    private String fieldAmount;
    @JsonProperty("field-cash-amount")
    private String fieldCashAmount;
    @JsonProperty("field-fees")
    private String fieldFees;
    @JsonProperty("finished-at")
    private Long finishedAt;
    @JsonProperty("user-id")
    private Integer userId;
    private String source;
    private String state;
    @JsonProperty("canceled-at")
    private Integer canceledAt;
    private String exchange;
    private String batch;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
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

    public String getFieldAmount() {
        return fieldAmount;
    }

    public void setFieldAmount(String fieldAmount) {
        this.fieldAmount = fieldAmount;
    }

    public String getFieldCashAmount() {
        return fieldCashAmount;
    }

    public void setFieldCashAmount(String fieldCashAmount) {
        this.fieldCashAmount = fieldCashAmount;
    }

    public String getFieldFees() {
        return fieldFees;
    }

    public void setFieldFees(String fieldFees) {
        this.fieldFees = fieldFees;
    }

    public Long getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Long finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getCanceledAt() {
        return canceledAt;
    }

    public void setCanceledAt(Integer canceledAt) {
        this.canceledAt = canceledAt;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }
}
