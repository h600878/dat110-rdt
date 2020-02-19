package no.hvl.dat110.transport.rdt2;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IAdversary;

public class AdversaryRDT21 implements IAdversary {

	private static double CORRUPTPB = 0.4;
	
	public int delay () {
		return 0;
	}
	
	public Datagram process (Datagram datagram) {
		
		if (Math.random() < CORRUPTPB) {
			
			SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();
			segment.setChecksum(((byte)1)); // Now also corrupt ack/naks
			
			assert (segment.isCorrect() == false);
			System.out.print("*");
			
		} else {
			System.out.print("+");
			assert ((SegmentRDT21)datagram.getSegment()).isCorrect();
		}
		
		return datagram;
			
	}
}
