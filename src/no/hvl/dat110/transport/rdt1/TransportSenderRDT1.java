package no.hvl.dat110.transport.rdt1;

import java.util.concurrent.TimeUnit;

import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.*;

public class TransportSenderRDT1 extends TransportSender implements ITransportProtocolEntity {

	public TransportSenderRDT1(NetworkService ns) {
		super("TransportSender",ns);
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
			System.out.println("Transport sender RDT1 thread " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
