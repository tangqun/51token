package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class CurrencyPlaceOrder {

    @NotBlank(message = "msgId不能为空")
    @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "msgId格式错误")
    private String msgId;

    @NotBlank(message = "交易所必填")
    private String exchangeName;
    // 币种
    @NotBlank(message = "币种必填")
    private String currency;
    // 单价
    private Double price;
    // 数量
    private Double amount;
    // 转换币对，二期？

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
