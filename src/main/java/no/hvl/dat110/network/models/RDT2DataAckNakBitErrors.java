package no.hvl.dat110.network.models;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IChannelModel;
import no.hvl.dat110.transport.rdt21.SegmentRDT21;

public class RDT2DataAckNakBitErrors implements IChannelModel {

    private static final double CORRUPTPB = 0.4;

    public int delay() {
        return 0;
    }

    public Datagram process(String name, Datagram datagram) {

        if (Math.random() < CORRUPTPB) {

            SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();

            segment.setChecksum((byte) (~(segment.getChecksum())));

            System.out.println("[Network:" + name + "*   ] transmit: " + datagram);

        }
        else {
            System.out.println("[Network:" + name + "+   ] transmit: " + datagram.toString());
        }

        return datagram;
    }
}
