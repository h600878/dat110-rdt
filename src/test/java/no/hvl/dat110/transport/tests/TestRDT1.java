package no.hvl.dat110.transport.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.models.RDT1ReliableChannel;
import no.hvl.dat110.transport.rdt1.TransportReceiverRDT1;
import no.hvl.dat110.transport.rdt1.TransportSenderRDT1;

public class TestRDT1 {

	@Test
	public void test() {
		
		TestTransport ts = new TestTransport();
		
		ts.setupNetwork(new RDT1ReliableChannel());
		
		ts.setupTransport(new TransportSenderRDT1(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT1(ts.getNetwork().getReceiverService()));
		
		ts.runTest();
				
		ts.assertRDT();
		
	}

}
