package com.example.creditcardreader.service;

public class VerifyPinResult {
    private boolean success = true;
    private int retryCount = -1;

    public VerifyPinResult(boolean success,int count){
        this.success = success;
        this.retryCount = count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
