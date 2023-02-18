package no.hvl.dat110.transport.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.transport.rdt4.TransportSenderRDT4;
import no.hvl.dat110.network.models.RDT4DelayLossyBitErrors;
import no.hvl.dat110.transport.rdt4.TransportReceiverRDT4;
import no.hvl.dat110.transport.tests.TestTransport;

public class TestRDT4DelayLossyBitErrors {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setRunningTime(30000);

		ts.setupNetwork(new RDT4DelayLossyBitErrors());

		ts.setupTransport(new TransportSenderRDT4(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT4(ts.getNetwork().getReceiverService()));

		ts.runTest();

		ts.assertRDT();
	}

}
