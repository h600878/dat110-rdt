package no.hvl.dat110.network;

import no.hvl.dat110.transport.*;

public class NetworkService extends Stopable {

	private Channel chanin,chanout;
	private ITransportProtocolEntity transport;
	
	public NetworkService(String name, Channel chin,Channel chout) {
		super(name);
		chanin = chin;
		chanout = chout;
	}
	
	public void register(ITransportProtocolEntity transport) {
		this.transport = transport;
	}
	
	public void udt_send(Datagram datagram) {
				
		chanout.send(new Datagram(datagram.getSegment().clone()));
	}
	
	// TODO: better to expand this methods into the doProcess
	public void rdt_recv(Datagram datagram) {
		
		if (transport != null) {
			transport.rdt_recv(datagram.getSegment());
		}
	}
	
	public void doProcess() {
			
			Datagram datagram = chanin.receive();
			
			if (datagram != null) {
				rdt_recv(datagram);
			} else {
				// System.out.println("timeout");
			}
		} 
}