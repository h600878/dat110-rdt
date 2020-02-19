package no.hvl.dat110.transport.rdt1;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IChannelModel;

public class Adversary implements IChannelModel {

	public int delay () {
		return 0;
	}
	
	public Datagram process(Datagram datagram) {
		
		System.out.print("+");
		return datagram;
	}
}
