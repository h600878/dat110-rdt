package no.hvl.dat110.transport.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import no.hvl.dat110.network.models.RDT2DataAckNakBitErrors;
import no.hvl.dat110.network.models.RDT2DataBitErrors;
import no.hvl.dat110.transport.rdt21.TransportReceiverRDT21;
import no.hvl.dat110.transport.rdt21.TransportSenderRDT21;

public class TestRDT21DataAckNakBitErrors {

	@Test
	public void test() {

		TestTransport ts = new TestTransport();

		ts.setupNetwork(new RDT2DataAckNakBitErrors());

		ts.setupTransport(new TransportSenderRDT21(ts.getNetwork().getSenderService()), 
				new TransportReceiverRDT21(ts.getNetwork().getReceiverService()));

		ts.runTest();

		ts.assertRDT();
	}

}
