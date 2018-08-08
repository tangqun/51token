package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class SwitchKline {

    private Integer klineStatus;

    public Integer getKlineStatus() {
        return klineStatus;
    }

    public void setKlineStatus(Integer klineStatus) {
        this.klineStatus = klineStatus;
    }
}
