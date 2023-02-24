package no.hvl.dat110.transport.rdt21;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;

public class TransportSenderRDT21 extends TransportSender implements ITransportProtocolEntity {

    public enum RDT21SenderStates {
        WAITDATA0, WAITDATA1, WAITACKNAK0, WAITACKNAK1
    }

    private final LinkedBlockingQueue<SegmentRDT21> recvqueue;
    private RDT21SenderStates state;

    public TransportSenderRDT21(NetworkService ns) {
        super("TransportSender", ns);
        recvqueue = new LinkedBlockingQueue<>();
        state = RDT21SenderStates.WAITDATA0;
    }

    public void rdt_recv(Segment segment) {

        System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

        try {

            recvqueue.put((SegmentRDT21) segment);

        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private byte[] data = null;

    public void doProcess() {

        switch (state) {
            case WAITDATA0 -> doWaitData(0);
            case WAITDATA1 -> doWaitData(1);
            case WAITACKNAK0 -> doWaitAckNak(0);
            case WAITACKNAK1 -> doWaitAckNak(1);
        }

    }

    private void changeState(RDT21SenderStates newstate) {

        System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
        state = newstate;
    }

    private void doWaitData(int seqnr) {

        try {

            data = outdataqueue.poll(2, TimeUnit.SECONDS);

            if (data != null) { // something to send

                udt_send(new SegmentRDT21(data, seqnr));

                if (seqnr == 0) {
                    changeState(RDT21SenderStates.WAITACKNAK0);
                }
                else {
                    changeState(RDT21SenderStates.WAITACKNAK1);
                }

            }

        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void doWaitAckNak(int seqnr) {

        try {

            SegmentRDT21 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

            if (acksegment != null) { // something received via rdt_recv

                SegmentType type = acksegment.getType();

                if (!acksegment.isCorrect()) {

                    System.out.println("[Transport:Sender   ] BITERRORS");
                    udt_send(new SegmentRDT21(data, seqnr)); // Retransmit

                }
                else if (type == SegmentType.NAK) {

                    System.out.println("[Transport:Sender   ] NAK");
                    udt_send(new SegmentRDT21(data, seqnr)); // Retransmit

                }
                else {

                    System.out.println("[Transport:Sender   ] ACK ");

                    if (seqnr == 0) {
                        changeState(RDT21SenderStates.WAITDATA1);
                    }
                    else {
                        changeState(RDT21SenderStates.WAITDATA0);

                    }

                    data = null;
                }
            }
        }
        catch (InterruptedException ex) {
            System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
