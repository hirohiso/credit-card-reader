package com.example.creditcardreader.datasource;

public class FciAnalyzer {
    public Fci analyze(byte[] data){
        if(Byte.toUnsignedInt(data[0]) != 0x6F){
            throw new IllegalArgumentException();
        }
        Tlv fciTemp = Tlv.from(data);
        Tlv[] arr = fciTemp.getInnerTlv();

        return new Fci();
    }
}
