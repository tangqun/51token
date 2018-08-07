package com.sharex.token.api.entity.req;

import java.util.List;

public class ExchangeCurrencyCostEdit {

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
