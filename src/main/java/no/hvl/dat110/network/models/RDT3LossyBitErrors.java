package no.hvl.dat110.network.models;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IChannelModel;
import no.hvl.dat110.transport.rdt21.SegmentRDT21;;

public class RDT3LossyBitErrors implements IChannelModel {

	private static double CORRUPTPB = 0.2;
	private static double LOSSPB = 0.2;

	public int delay() {
		return 0;
	}

	public Datagram process(String name, Datagram datagram) {

		double rnd = Math.random();

		if (rnd <= CORRUPTPB) { // transmission error

			SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();
			segment.setChecksum((byte) (~(segment.getChecksum())));

			System.out.println("[Network:" + name + "*   ] transmit: " + datagram.toString());

		} else if (rnd <= CORRUPTPB + LOSSPB) { // loss

			System.out.println("[Network:" + name + "-   ] transmit: " + datagram.toString());

			datagram = null;

		} else { // success

			System.out.println("[Network:" + name + "+   ] transmit: " + datagram.toString());

		}

		return datagram;

	}
}
