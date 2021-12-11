package com.example.creditcardreader.datasource;

import javax.smartcardio.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * クレジットカードからPCSC経由でデータを読みとる
 */
public class CreditCardInfoPcscReader {
    public RecordDto readCard() {
        try {
            TerminalFactory tf = TerminalFactory.getDefault();
            CardTerminals ct = tf.terminals();
            System.out.println(ct.list().toString());

            CardTerminal terminal = ct.list().get(0);

            Card card = null;

            card = terminal.connect("*");
            System.out.println(card.toString());
            CardChannel channel = card.getBasicChannel();
            CardTransfer cardTransfer = new CardTransfer(card);
            ATR atr = card.getATR();
            Fci fci = selectAid(cardTransfer);
            Tl[] pdol = fci.pdol();
            Afl[] afl = processingOption(cardTransfer, pdol);
            return readRecord(cardTransfer, afl);

        } catch (CardException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void verifyPin(String pin){
        try {
            TerminalFactory tf = TerminalFactory.getDefault();
            CardTerminals ct = tf.terminals();
            System.out.println(ct.list().toString());

            CardTerminal terminal = ct.list().get(0);

            Card card = null;

            card = terminal.connect("*");
            System.out.println(card.toString());
            CardChannel channel = card.getBasicChannel();
            CardTransfer cardTransfer = new CardTransfer(card);
            ATR atr = card.getATR();
            Fci fci = selectAid(cardTransfer);
            Tl[] pdol = fci.pdol();
            Afl[] afl = processingOption(cardTransfer, pdol);
            //verify command
            if(pin.length() != 4){
                return;
            }
            for (char c : pin.toCharArray()){
                if(!Character.isDigit(c)){
                    return;
                }
            }
            String verify = "002000800824" + pin +"FFFFFFFFFF";
            BinaryData command = BinaryData.from(verify);
            ResponseAPDU answer = cardTransfer.rawCommand(new CommandAPDU(command.toByteArray()));
        } catch (CardException e) {
            e.printStackTrace();
            return;
        }
    }

    private RecordDto readRecord(CardTransfer cardTransfer, Afl[] afl) throws CardException {
        List<Tlv> list = new LinkedList<>();
        for (int i = 0; i < afl.length; i++) {
            int fi = afl[i].getFileIngicate();
            String temp = String.format("%02x", fi);
            int start = afl[i].getRecordStart();
            int end = afl[i].getRecordEnd();
            for (int j = start; j <= end; j++) {
                String num = String.format("%02x", j);
                String rrcmd = "00B2" + num + temp + "00";
                BinaryData binaryData = BinaryData.from(rrcmd);
                ResponseAPDU answer = cardTransfer.rawCommand(new CommandAPDU(binaryData.toByteArray()));
                Tlv tlv = Tlv.from(answer.getData());
                Tlv[] inner = tlv.getInnerTlv();
                for (int k = 0; k < inner.length; k++) {
                    list.add(inner[k]);
                }
            }
        }
        RecordDto dto = new RecordDto();

        BinaryData panTag = BinaryData.from("5A");
        BinaryData nameTag = BinaryData.from("5F20");
        BinaryData expDateTag = BinaryData.from("5F24");
        for(Tlv tlv : list){
            BinaryData tag = BinaryData.of(tlv.getTag());
            BinaryData value = BinaryData.of(tlv.getValue());
            if(panTag.equals(tag)){
                dto.setPan(value);
            }else if(nameTag.equals(tag)){
                dto.setCardholderName(value);
            }else if(expDateTag.equals(tag)){
                dto.setExpirationDate(value);
            }
            System.out.println("RR[tag]" + tag.toString());
            System.out.println("RR[value]" + value.toString());
        }
        return dto;
    }

    private Afl[] processingOption(CardTransfer transfer, Tl[] pdol) throws CardException {
        Map<BinaryData, BinaryData> map = new HashMap<>();
        BinaryData currencyCodeTag = BinaryData.from("9f1a");
        BinaryData currencyCode = BinaryData.from("0392");
        map.put(currencyCodeTag, currencyCode);
        String pdolHead = "80A80000";
        String pdolval = "";
        String pdolFoot = "00";
        for (int i = 0; i < pdol.length; i++) {
            BinaryData data = map.get(BinaryData.of(pdol[i].getTag()));
            pdolval += data.toString();
        }
        String temp = String.format("%02x", pdolval.length() / 2);
        String temp2 = String.format("%02x", pdolval.length() / 2 + 2);
        String pdolStr = pdolHead + temp2 + "83" + temp + pdolval + pdolFoot;
        BinaryData binaryData = BinaryData.from(pdolStr);
        ResponseAPDU answer = transfer.rawCommand(new CommandAPDU(binaryData.toByteArray()));
        return new GpoAnalyzer().analyze(answer.getData());
    }

    private Fci selectAid(CardTransfer transfer) throws CardException {
        //Select AID
        byte[] visa = {
                (byte) 0x00, (byte) 0xA4,
                (byte) 0x04, (byte) 0x00,
                (byte) 0x07,
                (byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x03, (byte) 0x10, (byte) 0x10,
                (byte) 0x00
        };

        ResponseAPDU answer = transfer.rawCommand(new CommandAPDU(visa));
        byte[] data = answer.getData();
        return new FciAnalyzer().analyze(data);
    }
}
