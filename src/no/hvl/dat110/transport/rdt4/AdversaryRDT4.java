package no.hvl.dat110.transport.rdt4;

import no.hvl.dat110.network.Datagram;
import no.hvl.dat110.transport.rdt3.AdversaryRDT3;

public class AdversaryRDT4 extends AdversaryRDT3 {
	
	private static double DELAYPB = 0.2;
	private static int DELAYLIMIT = 1000;
	
	public int delay () {
		
		int delay = 0;
		
		if (Math.random() <= DELAYPB) {
			delay = (int)(DELAYLIMIT * Math.random());
			System.out.print("<" + delay);
		} else {
			System.out.print(">");
		}
		
		return delay;
	}
	
	public Datagram process (Datagram datagram) {
		return (super.process(datagram));
	}
}
