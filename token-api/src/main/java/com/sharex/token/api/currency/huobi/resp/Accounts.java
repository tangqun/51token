package com.sharex.token.api.currency.huobi.resp;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Accounts {

//    {
//        "id": 4344135,
//        "type": "spot",
//        "subtype": "",
//        "state": "working"
//    }

    private Long id;
    private String type;
    @JsonProperty("subtype")
    private String subType;
    private String state;

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

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
