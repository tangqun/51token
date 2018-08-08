package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class CurrencyCancelOrder {

    @NotBlank(message = "交易所必填")
    private String exchangeName;
    // 币种
    @NotBlank(message = "币种必填")
    private String currency;
    //
    private String orderId;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
