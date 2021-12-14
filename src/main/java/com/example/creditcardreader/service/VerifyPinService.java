package com.example.creditcardreader.service;

import com.example.creditcardreader.datasource.CreditCardInfoPcscReader;

public class VerifyPinService {
    public VerifyPinResult verifyPin(String pin){
        CreditCardInfoPcscReader reader = new CreditCardInfoPcscReader();

        return reader.verifyPin(pin);
    }
}
