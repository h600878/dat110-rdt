package no.hvl.dat110.transport.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.models.RDT2DataBitErrors;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT2;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT2;

public class TestRDT2DataBitErrors {

	@Test
	public void test() {
		
		TestTransport ts = new TestTransport();

		ts.setupNetwork(new RDT2DataBitErrors());

		ts.setupTransport(new TransportSenderRDT2(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT2(ts.getNetwork().getReceiverService()));

		ts.runTest();

		ts.assertRDT();
	}

}
