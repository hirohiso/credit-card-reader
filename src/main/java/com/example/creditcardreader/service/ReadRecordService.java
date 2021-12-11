package com.example.creditcardreader.service;

import com.example.creditcardreader.datasource.CreditCardInfoPcscReader;
import com.example.creditcardreader.datasource.RecordDto;

public class ReadRecordService {
    public ReadRecordResult readRecord(){
        //クレジットカードからデータを読み込む
        CreditCardInfoPcscReader reader = new CreditCardInfoPcscReader();
        RecordDto dto = reader.readCard();

        ReadRecordResult result = new ReadRecordResult();
        result.setPan(dto.getPan().toString());
        result.setDate(dto.getExpirationDate().toString());
        result.setName(new String(dto.getCardholderName().toByteArray()));
        return result;
    }
}
