package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.NotBlank;

public class AssetAuth {

    @NotBlank(message = "apiKey不能为空")
    private String apiKey;
    @NotBlank(message = "apiSecret不能为空")
    private String apiSecret;
    @NotBlank(message = "交易所必填")
    private String exchangeName;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiSecret() {
        return apiSecret;
    }

    public void setApiSecret(String apiSecret) {
        this.apiSecret = apiSecret;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }
}
