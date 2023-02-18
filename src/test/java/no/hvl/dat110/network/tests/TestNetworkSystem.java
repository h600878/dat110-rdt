package no.hvl.dat110.network.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.Channel;
import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.network.models.RDT1ReliableChannel;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.network.*;

public class TestNetworkSystem {

	@Test
	public void test() {
		
		Channel ch1 = new Channel("channel 1", new RDT1ReliableChannel()); // TODO: operate with concept of bidirectional channel
		Channel ch2 = new Channel("channel 2", new RDT1ReliableChannel());
				
		NetworkService ns1 = new NetworkService("network service 1",ch1,ch2);
		NetworkService ns2 = new NetworkService("network service 2",ch2,ch1);
		
		ns1.start();
		ns2.start();
		
		String message1 = "1234";
		String message2 = "5678";
		
		Datagram datagram1 = new Datagram(new Segment (message1.getBytes()));
		Datagram datagram2 = new Datagram(new Segment (message2.getBytes()));
		
		ns1.udt_send(datagram1);
		ns2.udt_send(datagram2);
			
		try {
			Thread.sleep(5000); // leave time for reception			
		} catch (InterruptedException ex) {

			System.out.println("Test network " + ex.getMessage());
			ex.printStackTrace();

		}
		
		ns1.doStop();
		ns2.doStop();
	
	}
}
