package com.example.creditcardreader.service;

import com.example.creditcardreader.datasource.CreditCardInfoPcscReader;

public class ReadRecordService {

    public void readRecord(){
        //クレジットカードからデータを読み込む
        CreditCardInfoPcscReader reader = new CreditCardInfoPcscReader();
        reader.readCard();

    }
}
