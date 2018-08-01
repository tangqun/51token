package com.sharex.token.api.entity;

import java.util.List;

public class MyTrades {

    private List<MyTrade> buy;
    private List<MyTrade> sell;

    public List<MyTrade> getBuy() {
        return buy;
    }

    public void setBuy(List<MyTrade> buy) {
        this.buy = buy;
    }

    public List<MyTrade> getSell() {
        return sell;
    }

    public void setSell(List<MyTrade> sell) {
        this.sell = sell;
    }
}
