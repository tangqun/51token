package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class ExchangeCurrencyCostEdit {

    @NotBlank(message = "交易所必填")
    private String exchangeName;
    private List<CurrencyCostEdit> currencyCostEditList;

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public List<CurrencyCostEdit> getCurrencyCostEditList() {
        return currencyCostEditList;
    }

    public void setCurrencyCostEditList(List<CurrencyCostEdit> currencyCostEditList) {
        this.currencyCostEditList = currencyCostEditList;
    }
}
