package com.sharex.token.api.entity.req;

public class CurrencyCostEdit {

    private String currency;
    // 单价 or 总价
    // unit/total
    private String type;
    private Double cost;

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

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }
}
