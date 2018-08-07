package com.sharex.token.api.currency.huobi.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HistoryOrders {

    //{
    //    "status": "ok",
    //    "data": [{
    //        "id": 2213092281,
    //        "order-id": 9117237893,
    //        "match-id": 14541948961,
    //        "symbol": "ethusdt",
    //        "type": "sell-limit",
    //        "source": "web",
    //        "price": "421.800000000000000000",
    //        "filled-amount": "0.050000000000000000",
    //        "filled-fees": "0.042180000000000000",
    //        "filled-points": "0.0",
    //        "created-at": 1533180107178
    //    }]
    //}

    private Long id;
    @JsonProperty("order-id")
    private Long orderId;
    @JsonProperty("match-id")
    private Long matchId;
    private String symbol;
    private String type;
    private String source;
    private String price;
    @JsonProperty("filled-amount")
    private String filledAmount;
    @JsonProperty("filled-fees")
    private String filledFees;
    @JsonProperty("filled-points")
    private String filledPoints;
    @JsonProperty("created-at")
    private Long createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFilledAmount() {
        return filledAmount;
    }

    public void setFilledAmount(String filledAmount) {
        this.filledAmount = filledAmount;
    }

    public String getFilledFees() {
        return filledFees;
    }

    public void setFilledFees(String filledFees) {
        this.filledFees = filledFees;
    }

    public String getFilledPoints() {
        return filledPoints;
    }

    public void setFilledPoints(String filledPoints) {
        this.filledPoints = filledPoints;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }
}
