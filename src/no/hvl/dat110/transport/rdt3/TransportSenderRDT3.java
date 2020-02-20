package no.hvl.dat110.transport.rdt3;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

public class TransportSenderRDT3 extends TransportSender implements ITransportProtocolEntity {

	public enum RDT3SenderStates {
		WAITDATA0, WAITDATA1, WAITACK0, WAITACK1;
	}

	private LinkedBlockingQueue<SegmentRDT3> recvqueue;
	private RDT3SenderStates state;

    // flag indicating whether a timeout has occurred
	private RetransmissionTimer retranstimer;

	public TransportSenderRDT3(NetworkService ns) {
		super("TransportSender",ns);
		recvqueue = new LinkedBlockingQueue<SegmentRDT3>();
		state = RDT3SenderStates.WAITDATA0;
		retranstimer = new RetransmissionTimer();
	}

	public void rdt_recv(Segment segment) {

		System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

		try {

			recvqueue.put((SegmentRDT3) segment);

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

	private void changeState(RDT3SenderStates newstate) {

		System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
		state = newstate;
	}

	private void doWaitData(int seqnr) {

		try {

			data = outdataqueue.poll(2, TimeUnit.SECONDS);

			if (data != null) { // something to send

				udt_send(new SegmentRDT3(data, seqnr));

				retranstimer.start();

				if (seqnr == 0) {
					changeState(RDT3SenderStates.WAITACK0);
				} else {
					changeState(RDT3SenderStates.WAITACK1);
				}

			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
			ex.printStackTrace();
		}

		try {

			SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) {
				System.out.println("[Transport:Sender   ] DISCARD: " + acksegment.toString());
			}

		} catch (InterruptedException ex) {
			System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	private void doWaitAck(int seqnr) {

		if (retranstimer.timeout()) {
			
			System.out.println("[Transport:Sender   ] RETRANSMIT ");
			
			udt_send(new SegmentRDT3(data, seqnr)); // retransmit

			retranstimer.start();
		}

		try {

			SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) { // something received via rdt_recv

				if (acksegment.isCorrect() && (acksegment.getSeqnr() == seqnr)) {

					retranstimer.stop();
					
					System.out.println("[Transport:Sender   ] ACK");

					if (seqnr == 0) {
						changeState(RDT3SenderStates.WAITDATA1);
					} else {
						changeState(RDT3SenderStates.WAITDATA0);
					}

					data = null;
				}

				// if wrong seq nr or corrupt - just wait for timeout
			}
		} catch (InterruptedException ex) {

			System.out.println("TransportSenderRDT2 thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
