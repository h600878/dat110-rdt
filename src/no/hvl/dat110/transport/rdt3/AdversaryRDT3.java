package no.hvl.dat110.transport.rdt3;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.network.IChannelModel;
import no.hvl.dat110.transport.rdt2.SegmentRDT21;;

public class AdversaryRDT3 implements IChannelModel {

	private static double CORRUPTPB = 0.2;
	private static double LOSSPB = 0.2;
	
	public int delay () {
		return 0;
	}
	
	public Datagram process (Datagram datagram) {
		
		double rnd = Math.random();
		
		if (rnd <= CORRUPTPB) { // transmission error
			
			SegmentRDT21 segment = (SegmentRDT21) datagram.getSegment();
			segment.setChecksum(((byte)1)); 
			
			System.out.print("*");
			
		} else if (rnd <= CORRUPTPB + LOSSPB ) { // loss
		
			datagram = null; 
			System.out.print("-");
			
		} else { // success
			
			System.out.print("+");
			
		}
		
		return datagram;
			
	}
}
