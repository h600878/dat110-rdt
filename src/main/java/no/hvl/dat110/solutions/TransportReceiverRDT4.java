package no.hvl.dat110.solutions;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;
import no.hvl.dat110.transport.rdt3.SegmentRDT3;

public class TransportReceiverRDT4 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT4ReceiverStates {
		WAITING;
	}

	private RDT4ReceiverStates state;
	
	// sequence number of data segment expected next
	private int seqnr;

	public TransportReceiverRDT4(NetworkService ns) {
		super("TransportReceiver",ns);
		state = RDT4ReceiverStates.WAITING;
		seqnr = 0;
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
	
	private void doWaiting() {

		SegmentRDT3 segment = null;

		try {

			segment = (SegmentRDT3) insegmentqueue.poll(2, TimeUnit.SECONDS);

		} catch (InterruptedException ex) {
			System.out.println("TransportReceiver RDT4 - doProcess " + ex.getMessage());
			ex.printStackTrace();
		}

		if (segment != null) {

			if (segment.isCorrect() && (segment.getSeqnr() == seqnr)) {

				// deliver data to the application layer
				deliver_data(segment.getData());

				seqnr = seqnr + 1;
			}

			// send an ack to the sender indicating expected sequence number
			udt_send(new SegmentRDT3(SegmentType.ACK, seqnr));

		}
	}
}
