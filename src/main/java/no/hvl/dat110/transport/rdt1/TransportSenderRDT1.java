package no.hvl.dat110.transport.rdt1;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

public class TransportSenderRDT1 extends TransportSender implements ITransportProtocolEntity {

    public TransportSenderRDT1(NetworkService ns) {
        super("TransportSender", ns);
    }

    @Override
    public void rdt_recv(Segment segment) {
        // do not do anything in the basic transport sender entity
    }

    @Override
    public void doProcess() {

        try {

            // Venter i opptil 2 sekunder på at det skal komme data fra applikasjonslaget
            byte[] data = outdataqueue.poll(2, TimeUnit.SECONDS);

            // Hvis det er data, sender vi det videre til nettverkslaget
            if (data != null) {
                udt_send(new Segment(data));
            }

        }
        catch (InterruptedException ex) {
            System.out.println("Transport sender RDT1 thread " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
