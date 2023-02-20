package no.hvl.dat110.transport.tests;

import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.models.RDT2DataAckNakBitErrors;
import no.hvl.dat110.network.models.RDT2DataBitErrors;
import no.hvl.dat110.transport.rdt22.TransportReceiverRDT22;
import no.hvl.dat110.transport.rdt22.TransportSenderRDT22;
import no.hvl.dat110.transport.tests.TestTransport;

public class TestRDT22DataAckBitErrors {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setupNetwork(new RDT2DataAckNakBitErrors());

		ts.setupTransport(new TransportSenderRDT22(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT22(ts.getNetwork().getReceiverService()));

		ts.runTest();

		ts.assertRDT();
	}

}
