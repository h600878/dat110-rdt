package no.hvl.dat110.transport.tests;

import no.hvl.dat110.network.models.RDT1ReliableChannel;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT2;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT2;
import org.junit.jupiter.api.Test;

public class TestRDT2ReliableChannel {

    @Test
    public void test() {

        TestTransport ts = new TestTransport();

        ts.setupNetwork(new RDT1ReliableChannel());

        ts.setupTransport(new TransportSenderRDT2(ts.getNetwork().getSenderService()),
                new TransportReceiverRDT2(ts.getNetwork().getReceiverService()));

        ts.runTest();

        ts.assertRDT();
    }

}
