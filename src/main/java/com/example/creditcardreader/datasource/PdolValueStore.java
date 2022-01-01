package com.example.creditcardreader.datasource;

import java.util.HashMap;
import java.util.Map;

/**
 * PDOL構成用のValue値のstore
 *
 * 端末側で設定するタグ値とバリュー値のリストを保持
 */
public class PdolValueStore {
    private Map<BinaryData,BinaryData> map = new HashMap<>();

    public PdolValueStore defaultStore(){
        PdolValueStore store = new PdolValueStore();

        BinaryData currencyCodeTag = BinaryData.from("9f1a");
        BinaryData currencyCode = BinaryData.from("0392");
        store.map.put(currencyCodeTag,currencyCode);
        return store;
    }

    /**
     * タグ値に対応する値を返却する
     */
    BinaryData getValue(BinaryData tag){
        return map.get(tag);
    }
}
