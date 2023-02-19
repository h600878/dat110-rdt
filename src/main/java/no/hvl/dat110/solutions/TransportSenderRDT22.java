package no.hvl.dat110.solutions;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;
import no.hvl.dat110.solutions.SegmentRDT22;

public class TransportSenderRDT22 extends TransportSender implements ITransportProtocolEntity {

	public enum RDT22SenderStates {
		WAITDATA0, WAITDATA1, WAITACK0, WAITACK1;
	}

	private LinkedBlockingQueue<SegmentRDT22> recvqueue;
	private RDT22SenderStates state;

	public TransportSenderRDT22(NetworkService ns) {
		super("TransportSender",ns);
		recvqueue = new LinkedBlockingQueue<SegmentRDT22>();
		state = RDT22SenderStates.WAITDATA0;
	}

	public void rdt_recv(Segment segment) {

		System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

		try {

			recvqueue.put((SegmentRDT22) segment);

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private byte[] data = null;

	public void doProcess() {

		switch (state) {

		case WAITDATA0:

			doWaitData(0);

			break;

		case WAITDATA1:

			doWaitData(1);

			break;

		case WAITACK0:

			doWaitAck(0);

			break;

		case WAITACK1:

			doWaitAck(1);

			break;

		default:
			break;
		}

	}

	private void changeState(RDT22SenderStates newstate) {

		System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
		state = newstate;
	}

	private void doWaitData(int seqnr) {

		try {

			data = outdataqueue.poll(2, TimeUnit.SECONDS);

			if (data != null) { // something to send

				udt_send(new SegmentRDT22(data, seqnr));

				if (seqnr == 0) {
					changeState(RDT22SenderStates.WAITACK0);
				} else {
					changeState(RDT22SenderStates.WAITACK1);
				}

			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void doWaitAck(int seqnr) {

		try {

			SegmentRDT22 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) { // something received via rdt_recv

				SegmentType type = acksegment.getType();

				if (!acksegment.isCorrect()) {

					System.out.println("[Transport:Sender   ] CRP");
					udt_send(new SegmentRDT22(data, seqnr)); // retransmit

				} else if (acksegment.getSeqnr() != seqnr) {

					System.out.println("[Transport:Sender   ] ACK-RT");
					udt_send(new SegmentRDT22(data, seqnr)); // retransmit

				} else {

					System.out.println("[Transport:Sender   ] ACK ");

					if (seqnr == 0) {
						changeState(RDT22SenderStates.WAITDATA1);
					} else {
						changeState(RDT22SenderStates.WAITDATA0);

					}

					data = null;
				}
			}
		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
