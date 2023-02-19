package no.hvl.dat110.rdt22;

import org.junit.Test;

import no.hvl.dat110.network.models.RDT2DataAckNakBitErrors;
import no.hvl.dat110.network.models.RDT2DataBitErrors;
import no.hvl.dat110.transport.rdt21.TransportReceiverRDT21;
import no.hvl.dat110.transport.rdt21.TransportSenderRDT21;
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
