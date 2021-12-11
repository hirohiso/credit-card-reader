package com.example.creditcardreader.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GpoAnalyzerTest {
    @Test
    public void test1(){
        String data = "80125c00080101001001030018010200500103039000";
        byte[] arr= BinaryData.from(data).toByteArray();
        GpoAnalyzer analyzer = new GpoAnalyzer();
        analyzer.analyze(arr);
    }

}