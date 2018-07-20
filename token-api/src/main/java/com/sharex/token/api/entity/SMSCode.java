package com.sharex.token.api.entity;

import java.util.Date;

public class SMSCode {

    private Integer id;
    private String mobileNum;
    private String code;
    private Integer status;
    private String aliRequestId;
    private String aliBizId;
    private String aliCode;
    private String aliMessage;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAliRequestId() {
        return aliRequestId;
    }

    public void setAliRequestId(String aliRequestId) {
        this.aliRequestId = aliRequestId;
    }

    public String getAliBizId() {
        return aliBizId;
    }

    public void setAliBizId(String aliBizId) {
        this.aliBizId = aliBizId;
    }

    public String getAliCode() {
        return aliCode;
    }

    public void setAliCode(String aliCode) {
        this.aliCode = aliCode;
    }

    public String getAliMessage() {
        return aliMessage;
    }

    public void setAliMessage(String aliMessage) {
        this.aliMessage = aliMessage;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
