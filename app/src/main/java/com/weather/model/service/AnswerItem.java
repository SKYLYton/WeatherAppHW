package com.weather.model.service;

public class AnswerItem {
    private final String result;
    private final int code;

    public AnswerItem(String result, int code) {
        this.result = result;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }
}
