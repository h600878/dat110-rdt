package no.hvl.dat110.network;

public interface IChannelModel {

	// msecs to delay packet
	public int delay ();
	
	public Datagram process(Datagram datagram);
	
}
