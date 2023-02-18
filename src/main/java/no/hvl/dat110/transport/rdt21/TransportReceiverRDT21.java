package no.hvl.dat110.transport.rdt21;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;

public class TransportReceiverRDT21 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT21ReceiverStates {
		WAITING0, WAITING1;
	}
	
	private RDT21ReceiverStates state;

	public TransportReceiverRDT21(NetworkService ns) {
		super("TransportReceiver",ns);
		state = RDT21ReceiverStates.WAITING0;
	}
		
	private void changeState(RDT21ReceiverStates newstate ) {
		
		System.out.println("[Transport:Receiver ] " + state + "->" + newstate);
		state = newstate;
	}

	private void doWaiting(int seqnr) {
		
		SegmentRDT21 segment = null;
		
		try {
	
			segment = (SegmentRDT21)insegmentqueue.poll(2, TimeUnit.SECONDS);

		} catch (InterruptedException ex) {
			System.out.println("TransportReceiver RDT2 - doWaiting " + ex.getMessage());
			ex.printStackTrace();
		}
		
		if (segment != null) {

			if (segment.isCorrect() && (segment.getSeqnr() == seqnr)) {

				// deliver data to the transport layer
				deliver_data(segment.getData());

				// send an ack to the sender
				udt_send(new SegmentRDT21(SegmentType.ACK));
				
				// change state waiting for data segmemt with other bit
				if (seqnr == 0) {
					changeState(RDT21ReceiverStates.WAITING1);
				} else {
					changeState(RDT21ReceiverStates.WAITING0);
				}
				
			} else if ((segment.isCorrect() && (segment.getSeqnr() != seqnr))) {
				// send an ack to the sender
				udt_send(new SegmentRDT21(SegmentType.ACK));
			} else {
				System.out.println("[Transport:Receiver ] BITERRORS");
				udt_send(new SegmentRDT21(SegmentType.NAK));
				
			}
		}
	}

	
	public void doProcess() {

		switch (state) {

		case WAITING0:

			doWaiting(0);
			break;
			
		case WAITING1:
			
			doWaiting(1);
			break;
			
		default:
			break;
		}
	}
}
