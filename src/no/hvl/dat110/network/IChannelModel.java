package no.hvl.dat110.network;

public interface IChannelModel {

	// millseconds to delay packet 
	public int delay ();
	
	// callback when transmitting a datagram
	public Datagram process(String str, Datagram datagram);
	
}
