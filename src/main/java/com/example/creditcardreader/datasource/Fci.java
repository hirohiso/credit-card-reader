package com.example.creditcardreader.datasource;

public class Fci {
    //DFNAME
    byte[] dfName;
    //Application Label
    byte[] appLabel;
    //Application Priority Indicator
    byte[] api;
    //PDOL
    byte[] pdol;


    public String dfName(){
        return BinaryData.of(dfName).toString();
    }
    public String apLabel(){
        return new String(appLabel);
    }
}
