package no.hvl.dat110.network;

import no.hvl.dat110.common.Stopable;
import no.hvl.dat110.transport.*;

public class NetworkService extends Stopable {

	private Channel chanin, chanout;
	private ITransportProtocolEntity transport;

	public NetworkService(String name, Channel chin, Channel chout) {
		super(name);
		chanin = chin;
		chanout = chout;
	}

	public void register(ITransportProtocolEntity transport) {
		this.transport = transport;
	}

	public void udt_send(Datagram datagram) {

		chanout.transmit(new Datagram(datagram.getSegment().clone()));
	}

	public void doProcess() {

		Datagram datagram = chanin.receive();

		if ((datagram != null) && (transport != null)) {
			transport.rdt_recv(datagram.getSegment());
		}
	}
}