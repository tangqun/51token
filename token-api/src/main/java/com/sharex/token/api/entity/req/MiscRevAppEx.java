package com.sharex.token.api.entity.req;

import java.util.Date;

public class MiscRevAppEx {

    private String mobileSeries;
    private String osVersion;
    private Date occurTime;
    private String message;

    public String getMobileSeries() {
        return mobileSeries;
    }

    public void setMobileSeries(String mobileSeries) {
        this.mobileSeries = mobileSeries;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Date getOccurTime() {
        return occurTime;
    }

    public void setOccurTime(Date occurTime) {
        this.occurTime = occurTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
