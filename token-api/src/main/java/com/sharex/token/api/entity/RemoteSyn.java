package com.sharex.token.api.entity;

public class RemoteSyn<T> {

    private Long ts;
    private T data;

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
