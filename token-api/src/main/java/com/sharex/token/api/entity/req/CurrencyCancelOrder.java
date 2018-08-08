package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;

public class CurrencyCancelOrder {

    @NotBlank(message = "msgId不能为空")
    @Pattern(regexp = "^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$", message = "msgId格式错误")
    private String msgId;

    @NotBlank(message = "交易所必填")
    private String exchangeName;
    // 币种
    @NotBlank(message = "币种必填")
    private String currency;
    //
    private String orderId;

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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
