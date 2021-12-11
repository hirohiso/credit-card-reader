package com.example.creditcardreader.datasource;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class TlvTest {
    @Test
    void test1(){
        byte[] arr = {
            (byte)0x6f,(byte)0x01,(byte)0x03
        };
        byte[] tag = {
                (byte)0x6f
        };
        byte[] value = {
                (byte)0x03
        };

        Tlv tlv = Tlv.from(arr);
        assertTrue(Arrays.equals(tag,tlv.getTag()));
        assertTrue(Arrays.equals(value,tlv.getValue()));
        assertEquals(tlv.length(),3);
    }
    @Test
    void test2(){
        byte[] arr = {
                (byte)0x9f,(byte)0x38,(byte)0x02,(byte)0x03,(byte)0x04
        };
        byte[] tag = {
                (byte)0x9f,(byte)0x38
        };
        byte[] value = {
                (byte)0x03,(byte)0x04
        };

        Tlv tlv = Tlv.from(arr);
        assertTrue(Arrays.equals(tag,tlv.getTag()));
        assertTrue(Arrays.equals(value,tlv.getValue()));
        assertEquals(tlv.length(),5);
    }

}