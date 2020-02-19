package no.hvl.dat110.transport.rdt2;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IAdversary;

public class AdversaryRDT2 implements IAdversary {

	private static double CORRUPTPB = 0.4;
	
	public int delay () {
		return 0;
	}
	
	public Datagram process (Datagram datagram) {
		
		if (Math.random() < CORRUPTPB) {
			
			SegmentRDT2 segment = (SegmentRDT2) datagram.getSegment();
			segment.setChecksum(((byte)0)); // 0 is correct checksum on ack/nak
			
			System.out.print("*");
			
		} else {
			System.out.print("+");
		}
		
		return datagram;
			
	}
}
