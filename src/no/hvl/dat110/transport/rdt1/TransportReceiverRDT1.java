package no.hvl.dat110.transport.rdt1;

import no.hvl.dat110.transport.*;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class TransportReceiverRDT1 extends TransportReceiver implements ITransportProtocolEntity {

	public TransportReceiverRDT1() {
		super("TransportReceiver");
	}

	public void doProcess() {

		try {
			
			Segment segment = insegqueue.poll(2, TimeUnit.SECONDS);

			if (segment != null) {
				deliver_data(segment.getData());
			}
			
		} catch (InterruptedException ex) {
			System.out.println("Transport receiver - deliver data " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
