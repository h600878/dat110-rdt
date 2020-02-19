package no.hvl.dat110.transport.rdt2;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.transport.*;

public class TransportReceiverRDT2 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT2ReceiverStates {
		WAITING;
	}

	private RDT2ReceiverStates state;

	public TransportReceiverRDT2() {
		super("TransportReceiver");
		state = RDT2ReceiverStates.WAITING;
	}

	public void doWaiting() {

		SegmentRDT2 segment = null;

		try {

			segment = (SegmentRDT2)insegqueue.poll(2, TimeUnit.SECONDS);

		} catch (InterruptedException ex) {
			System.out.println("TransportReceiver RDT2 - doProcess " + ex.getMessage());
			ex.printStackTrace();
		}

		if (segment != null) {

			SegmentType acktype = SegmentType.NAK;

			if (segment.isCorrect()) {

				// deliver data to the transport layer
				deliver_data(segment.getData());

				// send an ack to the sender
				acktype = SegmentType.ACK;

			}

			udt_send(new SegmentRDT2(acktype));
		}

	}

	public void doProcess() {

		switch (state) {

		case WAITING:
			doWaiting();
			break;
		default:
			break;
		}
	}
}
