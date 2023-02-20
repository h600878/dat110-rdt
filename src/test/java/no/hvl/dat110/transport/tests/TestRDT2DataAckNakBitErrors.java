package no.hvl.dat110.transport.tests;

import no.hvl.dat110.network.models.RDT2DataAckNakBitErrors;
import no.hvl.dat110.transport.rdt2.TransportReceiverRDT2;
import no.hvl.dat110.transport.rdt2.TransportSenderRDT2;
import org.junit.jupiter.api.Test;

public class TestRDT2DataAckNakBitErrors {

    @Test
    public void test() {

        TestTransport ts = new TestTransport();

        ts.setupNetwork(new RDT2DataAckNakBitErrors());

        ts.setupTransport(new TransportSenderRDT2(ts.getNetwork().getSenderService()),
                new TransportReceiverRDT2(ts.getNetwork().getReceiverService()));

        ts.runTest();

        ts.assertRDT();
    }

}
