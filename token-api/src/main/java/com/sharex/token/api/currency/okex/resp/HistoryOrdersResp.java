package com.sharex.token.api.currency.okex.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class HistoryOrdersResp {

    private Boolean result;
    private Integer total;
    @JsonProperty("currency_page")
    private Integer currencyPage;
    @JsonProperty("page_length")
    private Integer pageLength;

    private List<OrdersInfo> orders;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getCurrencyPage() {
        return currencyPage;
    }

    public void setCurrencyPage(Integer currencyPage) {
        this.currencyPage = currencyPage;
    }

    public Integer getPageLength() {
        return pageLength;
    }

    public void setPageLength(Integer pageLength) {
        this.pageLength = pageLength;
    }

    public List<OrdersInfo> getOrders() {
        return orders;
    }

    public void setOrders(List<OrdersInfo> orders) {
        this.orders = orders;
    }
}
