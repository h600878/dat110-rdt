package no.hvl.dat110.transport.rdt3;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;

public class TransportReceiverRDT3 extends TransportReceiver implements ITransportProtocolEntity {

	public enum RDT3ReceiverStates {
		WAITING0, WAITING1;
	}
	
	private RDT3ReceiverStates state;
	
	public TransportReceiverRDT3(NetworkService ns) {
		super("TransportReceiver",ns);
		state = RDT3ReceiverStates.WAITING0;
	}
	
	private void changeState(RDT3ReceiverStates newstate ) {
		
		System.out.println("[Transport:Receiver ] " + state + "->" + newstate);
		state = newstate;
	}

	private void doWaiting(int seqnr) {
		
		SegmentRDT3 segment = null;
		
		try {
	
			segment = (SegmentRDT3)insegmentqueue.poll(2, TimeUnit.SECONDS);

		} catch (InterruptedException ex) {
			System.out.println("TransportReceiver RDT3 - doProcess " + ex.getMessage());
			ex.printStackTrace();
		}
		
		if (segment != null) {

			if (segment.isCorrect() && (segment.getSeqnr() == seqnr)) {

				// deliver data to the transport layer
				deliver_data(segment.getData());

				// send an ack to the sender
				udt_send(new SegmentRDT3(SegmentType.ACK,seqnr)); 
				
				// change state waiting for data segmemt with other bit
				if (seqnr == 0) {
					changeState(RDT3ReceiverStates.WAITING1);
				} else {
					changeState(RDT3ReceiverStates.WAITING0);
				}
				
			} else {
				
				// send an ack to the sender with the seqnr correctly received
				udt_send(new SegmentRDT3(SegmentType.ACK,1-seqnr));	
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
