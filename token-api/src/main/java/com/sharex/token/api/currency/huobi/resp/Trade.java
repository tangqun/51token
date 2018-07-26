package com.sharex.token.api.currency.huobi.resp;

public class Trade {

    /**
     * id : 600848670
     * price : 7962.62
     * amount : 0.0122
     * direction : buy
     * ts : 1489464451000
     */
    private Double amount;
    private Long ts;
    private Long id;
    private Double price;
    private String direction;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
