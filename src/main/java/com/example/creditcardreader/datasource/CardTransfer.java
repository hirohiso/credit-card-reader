package com.example.creditcardreader.datasource;
import javax.smartcardio.*;

public class CardTransfer implements AutoCloseable {
    private final Card card;
    private final CardChannel cardChannel ;

    public CardTransfer(Card card) {
        this.card = card;
        this.cardChannel = card.getBasicChannel();
    }

    public static CardTransfer getInstance() throws CardException {
        TerminalFactory tf = TerminalFactory.getDefault();
        CardTerminals ct = tf.terminals();
        CardTerminal terminal = ct.list().get(0);
        Card card = null;
        card = terminal.connect("*");
        CardChannel channel = card.getBasicChannel();
       return new CardTransfer(card);
    }

    public void selectFile() throws CardException {
        BinaryData cla = BinaryData.from("00");
        BinaryData ins = BinaryData.from("A4");
        BinaryData p1 = BinaryData.from("04");
        BinaryData p2 = BinaryData.from("00");
        BinaryData data = BinaryData.from("A000000003101000");
        BinaryData lc = BinaryData.of(data.length());
        BinaryData le = BinaryData.from("00");

        BinaryData command = cla.append(ins).append(p1).append(p2).append(lc).append(data).append(le);
        CommandAPDU selectCommand = new CommandAPDU(command.toByteArray());
        ResponseAPDU responce = transmit(selectCommand);

    }

    public ResponseAPDU rawCommand(CommandAPDU commandAPDU) throws CardException{
        return transmit(commandAPDU);
    }

    private ResponseAPDU transmit(CommandAPDU var1) throws CardException {
        System.out.println("command: " + var1.toString());
        log(var1.getBytes());
        ResponseAPDU responce = cardChannel.transmit(var1);
        System.out.println("answer: " + responce.toString());
        log(responce.getBytes());
        return responce;
    }

    @Override
    public void close() throws Exception {
        card.disconnect(false);
    }

    private void log(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for(byte b: bytes){
            sb.append(String.format("%02x ", b));
        }
        System.out.println(
                sb.toString()
        );
    }
}
