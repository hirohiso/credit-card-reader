package com.example.creditcardreader.datasource;

import java.lang.reflect.Array;

public final class BinaryData {
    public static final BinaryData EMPTY = new BinaryData(new byte[]{});
    private final byte[] value;

    private BinaryData(byte[] value) {
        this.value = value;
    }

    public static BinaryData of(byte... bytes) {
        if(bytes == null){
            return EMPTY;
        }
        return new BinaryData(bytes);
    }

    public static BinaryData of(int... ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return BinaryData.of(bytes);
    }

    public static BinaryData from(String hex) {
        if (hex.length() % 2 != 0) {
            throw new IllegalArgumentException();
        }
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length() / 2; i++) {
            bytes[i] = parseByte(hex.substring(i * 2, (i + 1) * 2), 16);
        }
        return BinaryData.of(bytes);
    }

    public int length(){
        return this.value.length;
    }
    public BinaryData append(BinaryData other) {
        byte[] result = new byte[this.value.length + other.value.length];
        System.arraycopy(this.value,0,result,0,this.value.length);
        System.arraycopy(other.value,0,result,this.value.length,other.value.length);
        return new BinaryData(result);//
    }

    public BinaryData subBinaryData(int begin, int end) {
        return null;//
    }
    public int indexOf(BinaryData search){
        return 0;//
    }

    public byte byteAt(int index){
        return this.value[index];
    }

    public byte[] toByteArray(){
        return this.value.clone();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(byte b: value){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static byte parseByte(String s, int radix) throws NumberFormatException {
        int i = Integer.parseInt(s, radix);
        if (i >= 0 && i <= 256) {
            return (byte)i;
        } else {
            throw new NumberFormatException("Value out of range. Value:\"" + s + "\" Radix:" + radix);
        }
    }
}
