package no.hvl.dat110.transport.rdt2;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

public class TransportReceiverRDT2 extends TransportReceiver implements ITransportProtocolEntity {

    public enum RDT2ReceiverStates {
        WAITING
    }

    private final RDT2ReceiverStates state;

    public TransportReceiverRDT2(NetworkService ns) {
        super("TransportReceiver", ns);
        state = RDT2ReceiverStates.WAITING;
    }

    public void doWaiting() {

        SegmentRDT2 segment = null;

        try {

            segment = (SegmentRDT2) insegmentqueue.poll(2, TimeUnit.SECONDS);

        }
        catch (InterruptedException ex) {
            System.out.println("TransportReceiver RDT2 - doWaiting " + ex.getMessage());
            ex.printStackTrace();
        }

        if (segment != null) {

            SegmentType acktype = SegmentType.NAK;

            if (segment.isCorrect()) {

                // Deliver data to the transport layer
                deliver_data(segment.getData());

                // Send an ack to the sender
                acktype = SegmentType.ACK;

            }

            udt_send(new SegmentRDT2(acktype));
        }

    }

    @Override
    public void doProcess() {

        if (Objects.requireNonNull(state) == RDT2ReceiverStates.WAITING) {
            doWaiting();
        }
    }
}
