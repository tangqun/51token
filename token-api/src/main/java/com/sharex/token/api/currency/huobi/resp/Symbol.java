package com.sharex.token.api.currency.huobi.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Symbol {

    @JsonProperty("base-currency")
    private String baseCurrency;
    @JsonProperty("quote-currency")
    private String quoteCurrency;
    @JsonProperty("price-precision")
    private Integer pricePrecision;
    @JsonProperty("amount-precision")
    private Integer amountPrecision;
    @JsonProperty("symbol-partition")
    private String symbolPartition;
    private String symbol;

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }

    public Integer getPricePrecision() {
        return pricePrecision;
    }

    public void setPricePrecision(Integer pricePrecision) {
        this.pricePrecision = pricePrecision;
    }

    public Integer getAmountPrecision() {
        return amountPrecision;
    }

    public void setAmountPrecision(Integer amountPrecision) {
        this.amountPrecision = amountPrecision;
    }

    public String getSymbolPartition() {
        return symbolPartition;
    }

    public void setSymbolPartition(String symbolPartition) {
        this.symbolPartition = symbolPartition;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
