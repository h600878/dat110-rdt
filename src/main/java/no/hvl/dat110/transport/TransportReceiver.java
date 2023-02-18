package no.hvl.dat110.transport;

import java.util.concurrent.LinkedBlockingQueue;

import no.hvl.dat110.application.ReceiverProcess;
import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.NetworkService;

public abstract class TransportReceiver extends Stopable implements ITransportProtocolEntity {

	private ReceiverProcess receiver;
	private NetworkService ns;

	protected LinkedBlockingQueue<Segment> insegmentqueue;

	public TransportReceiver(String name, NetworkService ns) {
		super(name);
		insegmentqueue = new LinkedBlockingQueue<Segment>();
		ns.register(this);
		this.ns = ns;

	}

	public void register(ReceiverProcess receiver) {
		this.receiver = receiver;
	}
	
	public final void rdt_send(byte[] data) {

		// should never used in the current setting in the receiver
		throw new RuntimeException("rdt_send called in transport receiver");
	}

	// deliver_data should always deliver the data to the application layer receiver
	public final void deliver_data(byte[] data) {
		receiver.deliver_data(data);
	}

	// udt_send should always send the segment via the underlying network service
	public final void udt_send(Segment segment) {
		System.out.println("[Transport:Receiver ] udt_send: " + segment.toString());
		ns.udt_send(new Datagram(segment));
	}

	// underlying network service will call this method when segments arrive
	public final void rdt_recv(Segment segment) {

		System.out.println("[Transport:Receiver ] rdt_recv: " + segment.toString());

		try {
			insegmentqueue.put(segment);
		} catch (InterruptedException ex) {

			System.out.println("Transport receiver  " + ex.getMessage());
			ex.printStackTrace();
		}

	}
}
