package no.hvl.dat110.solutions;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.rdt3.SegmentRDT3;
import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.ITransportProtocolEntity;
import no.hvl.dat110.transport.TransportSender;
import no.hvl.dat110.transport.rdt3.RetransmissionTimer;

public class TransportSenderRDT4 extends TransportSender implements ITransportProtocolEntity {

		// queue of ack-segments received from the receiver
		private LinkedBlockingQueue<SegmentRDT3> recvqueue;

		// sender main-states
		public enum RDT4SenderStates {
			WAITDATA, WAITACK;
		}

		private RDT4SenderStates state;
		
		// data and sequence number currently being sent
		private byte[] data;
	    private int seqnr;

	    // timer for retransmission of data segments
		private RetransmissionTimer retranstimer;

		public TransportSenderRDT4(NetworkService ns) {
			super("TransportSender",ns);
			recvqueue = new LinkedBlockingQueue<SegmentRDT3>();
			
			state = RDT4SenderStates.WAITDATA;
			retranstimer = new RetransmissionTimer();
			
			data = null;
			seqnr = 0;
		}

		// invoked by network service when ack-segments comes from the receiver side
		public void rdt_recv(Segment segment) {

			System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

			try {

				recvqueue.put((SegmentRDT3) segment);

			} catch (InterruptedException ex) {
				System.out.println("TransportSenderRDT4 thread " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		public void doProcess() {

			switch (state) {

			case WAITDATA:

				doWaitData();

				break;

			case WAITACK:

				doWaitAck();

				break;

			default:
				break;
			}

		}

		private void changeState(RDT4SenderStates newstate) {

			System.out.println("[Transport:Sender   ] " + state + "->" + newstate);
			state = newstate;
		}

		
		private void doWaitData() {

			try {

				data = outdataqueue.poll(2, TimeUnit.SECONDS);

				if (data != null) { // something to send

					udt_send(new SegmentRDT3(data, seqnr));

					retranstimer.start();

					changeState(RDT4SenderStates.WAITACK);
				}

			} catch (InterruptedException ex) {
				System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
				ex.printStackTrace();
			}

			try {

				SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

				// we are waiting for data to send so just ignore any incombing acks
				if (acksegment != null) {
					System.out.println("[Transport:Sender   ] DISCARD: " + acksegment.toString());
				}

			} catch (InterruptedException ex) {
				System.out.println("TransportSenderRDT3 thread " + ex.getMessage());
				ex.printStackTrace();
			}

		}

		private void doWaitAck() {

			if (retranstimer.timeout()) {
				
				System.out.println("[Transport:Sender   ] RETRANSMIT ");
				
				udt_send(new SegmentRDT3(data, seqnr)); // retransmit

				retranstimer.start();
			}

			try {

				SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

				if (acksegment != null) { // something received via rdt_recv

					if (acksegment.isCorrect() && (acksegment.getSeqnr() > seqnr)) {

						retranstimer.stop();
						
						System.out.println("[Transport:Sender   ] ACK");
					
						seqnr = acksegment.getSeqnr();
						data = null;
						
						changeState(RDT4SenderStates.WAITDATA);

					}

					// if wrong seqnr or corrupt - just wait for timeout/retransmit of current data
				}
			} catch (InterruptedException ex) {

				System.out.println("TransportSenderRDT4 thread " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}
