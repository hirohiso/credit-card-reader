package com.example.creditcardreader.datasource;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AflTest {
    @Test
    public void test1(){
        String data = "08010100";
        byte[] arr= BinaryData.from(data).toByteArray();
        Afl afl = Afl.from(arr);

        System.out.println(String.format("%02x",afl.getFileIngicate()));
    }
    @Test
    public void test2(){
        String data = "10010300";
        byte[] arr= BinaryData.from(data).toByteArray();
        Afl afl = Afl.from(arr);
        System.out.println(String.format("%02x",afl.getFileIngicate()));
    }

}