package com.sharex.token.api.currency.okex.resp;

import java.math.BigInteger;

public class Trade {

    private Long date;
    private Long dateMs;
    private Double amount;
    private Double price;
    private String type;
    private BigInteger tid;

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDateMs() {
        return dateMs;
    }

    public void setDateMs(Long dateMs) {
        this.dateMs = dateMs;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigInteger getTid() {
        return tid;
    }

    public void setTid(BigInteger tid) {
        this.tid = tid;
    }
}
