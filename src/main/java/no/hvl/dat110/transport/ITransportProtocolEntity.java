package no.hvl.dat110.transport;

public interface ITransportProtocolEntity {

	// invoked when the application layer has data to be sent by the transport layer
	public void rdt_send(byte[] data);
		
	// invoked from transport layer to deliver data to the application layer
	public void deliver_data(byte[] data);
		
	// invoked to send data using the network layer
	public void udt_send(Segment segment);
		
	// invoked when the network layer has a segment for the transport layer
	public void rdt_recv(Segment segment);
	
}
