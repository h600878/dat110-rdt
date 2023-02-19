package no.hvl.dat110.solutions;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;
import no.hvl.dat110.solutions.SegmentRDT22;

public class TransportReceiverRDT22 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT21ReceiverStates {
		WAITING0, WAITING1;
	}
	
	private RDT21ReceiverStates state;

	public TransportReceiverRDT22(NetworkService ns) {
		super("TransportReceiver",ns);
		state = RDT21ReceiverStates.WAITING0;
	}
		
	private void changeState(RDT21ReceiverStates newstate ) {
		
		System.out.println("[Transport:Receiver ] " + state + "->" + newstate);
		state = newstate;
	}

	private void doWaiting(int seqnr) {
		
		SegmentRDT22 segment = null;
		
		try {
	
			segment = (SegmentRDT22)insegmentqueue.poll(2, TimeUnit.SECONDS);

		} catch (InterruptedException ex) {
			System.out.println("TransportReceiver RDT2 - doWaiting " + ex.getMessage());
			ex.printStackTrace();
		}
		
		if (segment != null) {

			if (segment.isCorrect() && (segment.getSeqnr() == seqnr)) {

				// deliver data to the transport layer
				deliver_data(segment.getData());

				// send an ack to the sender
				udt_send(new SegmentRDT22(SegmentType.ACK,seqnr));
				
				// change state waiting for data segment with other bit
				if (seqnr == 0) {
					changeState(RDT21ReceiverStates.WAITING1);
				} else {
					changeState(RDT21ReceiverStates.WAITING0);
				}
				
			} else {
				
				// not the segment we were waiting for - so send an ack what for what has been received
				udt_send(new SegmentRDT22(SegmentType.ACK,1-seqnr));
				
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
