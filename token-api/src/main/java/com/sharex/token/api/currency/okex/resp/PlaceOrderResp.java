package com.sharex.token.api.currency.okex.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlaceOrderResp {

    private Boolean result;
    @JsonProperty("order_id")
    private String orderId;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
