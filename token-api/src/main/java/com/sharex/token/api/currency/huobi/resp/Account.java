package com.sharex.token.api.currency.huobi.resp;

import java.util.List;

public class Account {
    private Long id;
    private String type;
    private String state;
    private List<Balance> list;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Balance> getList() {
        return list;
    }

    public void setList(List<Balance> list) {
        this.list = list;
    }
}

