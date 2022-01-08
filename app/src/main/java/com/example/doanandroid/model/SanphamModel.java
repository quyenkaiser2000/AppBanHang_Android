package com.example.doanandroid.model;

import java.util.List;

public class SanphamModel {
    boolean success;
    String message;
    List<Sanpham> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Sanpham> getResult() {
        return result;
    }

    public void setResult(List<Sanpham> result) {
        this.result = result;
    }
}
