package com.sharex.token.api.currency.okex.resp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo {

    private Info info;
    private Boolean result;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}

class Info {

    private Funds funds;

    public Funds getFunds() {
        return funds;
    }

    public void setFunds(Funds funds) {
        this.funds = funds;
    }
}

class Funds {

    private Borrow borrow;
    private Asset asset;
    private Free free;
    private Freezed freezed;

    public Borrow getBorrow() {
        return borrow;
    }

    public void setBorrow(Borrow borrow) {
        this.borrow = borrow;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public Free getFree() {
        return free;
    }

    public void setFree(Free free) {
        this.free = free;
    }

    public Freezed getFreezed() {
        return freezed;
    }

    public void setFreezed(Freezed freezed) {
        this.freezed = freezed;
    }
}

class Borrow {
    private String btc;
    private String etc;
    private String bch;
    private String eth;
    private String ltc;

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getBch() {
        return bch;
    }

    public void setBch(String bch) {
        this.bch = bch;
    }

    public String getEth() {
        return eth;
    }

    public void setEth(String eth) {
        this.eth = eth;
    }

    public String getLtc() {
        return ltc;
    }

    public void setLtc(String ltc) {
        this.ltc = ltc;
    }
}

class Asset {
    private String total;
    private String net;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNet() {
        return net;
    }

    public void setNet(String net) {
        this.net = net;
    }
}

class Free {
    private String btc;
    private String etc;
    private String bch;
    private String usd;
    private String eth;
    private String ltc;

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getBch() {
        return bch;
    }

    public void setBch(String bch) {
        this.bch = bch;
    }

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }

    public String getEth() {
        return eth;
    }

    public void setEth(String eth) {
        this.eth = eth;
    }

    public String getLtc() {
        return ltc;
    }

    public void setLtc(String ltc) {
        this.ltc = ltc;
    }
}

class Freezed {
    private String btc;
    private String etc;
    private String bch;
    private String usd;
    private String eth;
    private String ltc;

    public String getBtc() {
        return btc;
    }

    public void setBtc(String btc) {
        this.btc = btc;
    }

    public String getEtc() {
        return etc;
    }

    public void setEtc(String etc) {
        this.etc = etc;
    }

    public String getBch() {
        return bch;
    }

    public void setBch(String bch) {
        this.bch = bch;
    }

    public String getUsd() {
        return usd;
    }

    public void setUsd(String usd) {
        this.usd = usd;
    }

    public String getEth() {
        return eth;
    }

    public void setEth(String eth) {
        this.eth = eth;
    }

    public String getLtc() {
        return ltc;
    }

    public void setLtc(String ltc) {
        this.ltc = ltc;
    }
}
