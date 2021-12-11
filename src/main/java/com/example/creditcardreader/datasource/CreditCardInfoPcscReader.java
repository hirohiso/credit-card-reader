package com.example.creditcardreader.datasource;

import javax.smartcardio.*;

/**
 * クレジットカードからPCSC経由でデータを読みとる
 */
public class CreditCardInfoPcscReader {
    public void readCard() {
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
            selectAid(cardTransfer);
        } catch (CardException e) {
            e.printStackTrace();
            return;
        }
    }

    private void selectAid(CardTransfer transfer) throws CardException {
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
        System.out.println("");
    }
}
