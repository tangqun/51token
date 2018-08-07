package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class ExchangeOpenOrdersSyn {

    @NotBlank(message = "交易所必填")
    private String exchangeName;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
