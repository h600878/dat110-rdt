package no.hvl.dat110.transport.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.transport.rdt3.TransportSenderRDT3;
import no.hvl.dat110.network.models.RDT3LossyBitErrors;
import no.hvl.dat110.transport.rdt3.TransportReceiverRDT3;

public class TestRDT3LossyBitErrors {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setRunningTime(30000);
		
		ts.setupNetwork(new RDT3LossyBitErrors());

		ts.setupTransport(new TransportSenderRDT3(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT3(ts.getNetwork().getReceiverService()));

		ts.runTest();

		ts.assertRDT();
	}

}
