package no.hvl.dat110.transport.rdt1;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.transport.*;

public class TransportSenderRDT1 extends TransportSender implements ITransportProtocolEntity {

	

	public TransportSenderRDT1() {
		super("TransportSender");
	}
		
	public void rdt_recv(Segment segment) {

		// do not do anything in the basic transport sender entity
	}

	public void doProcess() {

		try {
			
			byte[] data = outdataqueue.poll(2, TimeUnit.SECONDS);

			if (data != null) {
				udt_send(new Segment(data));
			}

		} catch (InterruptedException ex) {
			System.out.println("Transport sender thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
