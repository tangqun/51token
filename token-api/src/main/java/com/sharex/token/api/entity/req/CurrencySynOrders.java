package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class CurrencySynOrders {

    @NotBlank(message = "交易所名称不能为空")
    private String exchangeName;
    @NotBlank(message = "数字币名称不能为空")
    private String currency;

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
}
