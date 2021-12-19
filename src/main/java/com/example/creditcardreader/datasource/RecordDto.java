package com.example.creditcardreader.datasource;

public class RecordDto {
    private String apLabel;
    private BinaryData pan;
    private BinaryData cardholderName;
    private BinaryData expirationDate;

    public BinaryData getPan() {
        return pan;
    }

    public void setPan(BinaryData pan) {
        this.pan = pan;
    }

    public BinaryData getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(BinaryData cardholderName) {
        this.cardholderName = cardholderName;
    }

    public BinaryData getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(BinaryData expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getApLabel() {
        return apLabel;
    }

    public void setApLabel(String apLabel) {
        this.apLabel = apLabel;
    }
}
