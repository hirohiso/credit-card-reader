package com.example.creditcardreader.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FciAnalyzerTest {
    @Test
    void test(){
        String data = "6f288407a0000000031010a51d500a564953414352454449548701019f38039f1a02bf0c059f4d020f0a";
        byte[] arr= BinaryData.from(data).toByteArray();
        FciAnalyzer analyzer = new FciAnalyzer();
        Fci fci = analyzer.analyze(arr);
        assertEquals("a0000000031010",fci.dfName());
        assertEquals("VISACREDIT",fci.apLabel());
        fci.pdol();
    }
    @Test
    void lab(){

        System.out.println(String.format("%02x", 0xF0| 0x04));
        System.out.println((byte)0xFF);
        System.out.println(Byte.toUnsignedInt((byte)-17));
        System.out.println((byte)0x6f);
    }

}