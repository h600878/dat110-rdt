package no.hvl.dat110.transport.rdt1;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

import java.util.concurrent.TimeUnit;

public class TransportReceiverRDT1 extends TransportReceiver implements ITransportProtocolEntity {

    public TransportReceiverRDT1(NetworkService ns) {
        super("TransportReceiver", ns);
    }

    @Override
    public void doProcess() {

        try {

            // Venter i opptil 2 sekunder på at det skal komme data fra nettverkslaget
            Segment segment = insegmentqueue.poll(2, TimeUnit.SECONDS);

            if (segment != null) {
                deliver_data(segment.getData());
            }

        }
        catch (InterruptedException ex) {
            System.out.println("Transport receiver RDT1 - doProcess " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
