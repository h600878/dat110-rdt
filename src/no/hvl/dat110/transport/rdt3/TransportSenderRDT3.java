package no.hvl.dat110.transport.rdt3;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt2.SegmentType;

public class TransportSenderRDT3 extends TransportSender implements ITransportProtocolEntity {

	public enum RDT3SenderStates {
		WAITDATA0, WAITDATA1, WAITACK0, WAITACK1;
	}

	private LinkedBlockingQueue<SegmentRDT3> recvqueue;
	private RDT3SenderStates state;

	public TransportSenderRDT3() {
		super("TransportSender");
		recvqueue = new LinkedBlockingQueue<SegmentRDT3>();
		state = RDT3SenderStates.WAITDATA0;
		timeout = false;
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

	// TODO: consider moving retransmission timer into seperate file
	private boolean timeout;
	private Timer timer;

	private void stop_timer() {
		timer.cancel();
		timeout = false;
	}
	
	public void start_timer() {
		timer = new Timer(); 
		timer.schedule(new TimeOutTask(), 1000);
		timeout = false;
	}
	
    class TimeOutTask extends TimerTask {
    	
        public void run() {
            System.out.println("[Transport:Sender   ] TIMEOUT");
            timer.cancel(); 
            timeout = true;
        }
    }
    
	private void doWaitData(int seqnr) {

		try {

			data = outdataqueue.poll(2, TimeUnit.SECONDS);

			if (data != null) { // something to send

				udt_send(new SegmentRDT3(data, seqnr));

				start_timer();

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

		if (timeout) {
			
			System.out.println("[Transport:Sender   ] RETRANSMIT ");
			
			udt_send(new SegmentRDT3(data, seqnr)); // retransmit

			start_timer();
		}

		try {

			SegmentRDT3 acksegment = recvqueue.poll(2, TimeUnit.SECONDS);

			if (acksegment != null) { // something received via rdt_recv

				if (acksegment.isCorrect() && (acksegment.getSeqnr() == seqnr)) {

					stop_timer();
					
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
