package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class SwitchKline {

    @NotBlank(message = "状态值必填")
    private Integer klineStatus;

    public Integer getKlineStatus() {
        return klineStatus;
    }

    public void setKlineStatus(Integer klineStatus) {
        this.klineStatus = klineStatus;
    }
}
