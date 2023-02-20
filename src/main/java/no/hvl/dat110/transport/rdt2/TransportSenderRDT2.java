package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

public class TransportSenderRDT2 extends TransportSender implements ITransportProtocolEntity {

    public enum RDT2SenderStates {
        WAITDATA, WAITACKNAK
    }

    private final LinkedBlockingQueue<SegmentRDT2> recvqueue;
    private RDT2SenderStates state;

    public TransportSenderRDT2(NetworkService ns) {
        super("TransportSender", ns);
        recvqueue = new LinkedBlockingQueue<>();
        state = RDT2SenderStates.WAITDATA;
    }

    @Override
    public void rdt_recv(Segment segment) {

        System.out.println("[Transport:Sender   ] rdt_recv: " + segment);

        try {
            recvqueue.put((SegmentRDT2) segment);
        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private byte[] data = null;

    @Override
    public void doProcess() {

        switch (state) {
            case WAITDATA -> doWaitData();
            case WAITACKNAK -> doWaitAckNak();
        }

    }

    private void changeState(RDT2SenderStates newstate) {

        System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
        state = newstate;
    }

    private void doWaitData() {

        try {

            data = outdataqueue.poll(2, TimeUnit.SECONDS);

            if (data != null) { // Something to send

                udt_send(new SegmentRDT2(data));

                changeState(RDT2SenderStates.WAITACKNAK);
            }

        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void doWaitAckNak() {

        try {

            SegmentRDT2 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

            if (acksegment != null) {

                SegmentType type = acksegment.getType();

                if (type == SegmentType.ACK) {

                    System.out.println("[Transport:Sender   ] ACK ");
                    data = null;
                    changeState(RDT2SenderStates.WAITDATA);

                }
                else {
                    System.out.println("[Transport:Sender   ] NAK ");
                    udt_send(new SegmentRDT2(data));
                }
            }

        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
