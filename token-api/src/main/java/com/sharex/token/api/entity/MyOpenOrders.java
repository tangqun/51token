package com.sharex.token.api.entity;

import java.util.Date;

public class MyOpenOrders {

    private String source;
    private String symbol;
    private String amount;
    private Long createdAt;
    private Date createdAtDisplay;
    private String price;
    private Long id;
    private String state;
    private Integer myState;
    private String stateDisplay;
    private String type;
    private String typeDisplay;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Date getCreatedAtDisplay() {
        return createdAtDisplay;
    }

    public void setCreatedAtDisplay(Date createdAtDisplay) {
        this.createdAtDisplay = createdAtDisplay;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getMyState() {
        return myState;
    }

    public void setMyState(Integer myState) {
        this.myState = myState;
    }

    public String getStateDisplay() {
        return stateDisplay;
    }

    public void setStateDisplay(String stateDisplay) {
        this.stateDisplay = stateDisplay;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypeDisplay() {
        return typeDisplay;
    }

    public void setTypeDisplay(String typeDisplay) {
        this.typeDisplay = typeDisplay;
    }
}
