package no.hvl.dat110.application;

import java.util.ArrayList;

import no.hvl.dat110.transport.TransportSender;

public class SenderProcess {

    private final ArrayList<byte[]> datasent;
    private final TransportSender transport;

    public SenderProcess(TransportSender transport) {
        this.transport = transport;
        datasent = new ArrayList<>();
    }

    public ArrayList<byte[]> getDatasent() {
        return datasent;
    }

    public void doRun() {

        for (int i = 0; i < 3; i++) {

            String message = "Message " + (i);

            System.out.println("[App:SenderProcess  ] rdt_send: " + "[" + message + "]");

            byte[] data = message.getBytes();

            transport.rdt_send(data);

            datasent.add(data);
        }
    }

}
