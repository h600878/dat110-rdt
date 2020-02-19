package no.hvl.dat110.network.models;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IChannelModel;

public class RDT1ReliableChannel implements IChannelModel {

	public int delay () {
		return 0;
	}
	
	public Datagram process(String name, Datagram datagram) {
		
		System.out.println("[Network:" + name + "+   ] transmit: " + datagram.toString());
		
		return datagram;
	}
}
