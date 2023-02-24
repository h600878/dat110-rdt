package no.hvl.dat110.application;

import java.util.ArrayList;

import no.hvl.dat110.transport.TransportReceiver;

public class ReceiverProcess {

    private final ArrayList<byte[]> datarecv;

    /**
     * @param transport The transport layer receiver
     */
    public ReceiverProcess(TransportReceiver transport) {
        // Regisrerer seg selv som mottaker av data
        transport.register(this);
        datarecv = new ArrayList<>();

    }

    public ArrayList<byte[]> getDatarecv() {
        return datarecv;
    }

    public void deliver_data(byte[] data) {

        datarecv.add(data);

        String message = new String(data);

        System.out.println("[App:ReceiverProcess] dlv_data: " + "[" + message + "]");
    }
}
