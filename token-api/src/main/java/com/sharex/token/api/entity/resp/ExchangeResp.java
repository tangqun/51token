package com.sharex.token.api.entity.resp;

public class ExchangeResp {

    private String logo;
    private String name;
    private String shortName;
    private String desc;
    private Integer authStatus;
    private String authTutorialUrl;
    private String officialUrl;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public String getAuthTutorialUrl() {
        return authTutorialUrl;
    }

    public void setAuthTutorialUrl(String authTutorialUrl) {
        this.authTutorialUrl = authTutorialUrl;
    }

    public String getOfficialUrl() {
        return officialUrl;
    }

    public void setOfficialUrl(String officialUrl) {
        this.officialUrl = officialUrl;
    }
}
