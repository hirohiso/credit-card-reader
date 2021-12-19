package com.example.creditcardreader.service;

public class ReadRecordResult {
    private String ap;
    private String pan;
    private String date;
    private String name;

    public String getPan() {
        return new StringBuilder(pan).replace(6,12,"******").toString();
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAp() {
        return ap;
    }

    public void setAp(String ap) {
        this.ap = ap;
    }
}
