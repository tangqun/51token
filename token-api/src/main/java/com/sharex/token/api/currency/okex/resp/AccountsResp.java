package com.sharex.token.api.currency.okex.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountsResp {

    private Boolean result;
    private AccountsInfo info;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public AccountsInfo getInfo() {
        return info;
    }

    public void setInfo(AccountsInfo info) {
        this.info = info;
    }
}
