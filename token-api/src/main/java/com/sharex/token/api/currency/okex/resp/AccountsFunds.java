package com.sharex.token.api.currency.okex.resp;

public class AccountsFunds {

    private AccountsFree free;
    private AccountsFree freezed;

    public AccountsFree getFree() {
        return free;
    }

    public void setFree(AccountsFree free) {
        this.free = free;
    }

    public AccountsFree getFreezed() {
        return freezed;
    }

    public void setFreezed(AccountsFree freezed) {
        this.freezed = freezed;
    }
}
