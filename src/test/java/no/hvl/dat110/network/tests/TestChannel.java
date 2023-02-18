package no.hvl.dat110.network.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.models.RDT1ReliableChannel;
import no.hvl.dat110.network.*;
import no.hvl.dat110.transport.*;

public class TestChannel {

	@Test
	public void test() {

		Channel channel = new Channel("Test channel", new RDT1ReliableChannel());
		
		byte[] data = { 1, 2, 3, 4 };
		
		Datagram datasend = new Datagram (new Segment(data));

		channel.transmit(datasend);

		Datagram datarecv = channel.receive();

		assertEquals(datasend, datarecv);

	}

}
