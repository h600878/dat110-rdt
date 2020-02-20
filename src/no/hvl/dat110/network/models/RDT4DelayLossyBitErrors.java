package no.hvl.dat110.network.models;

import no.hvl.dat110.network.Datagram;

public class RDT4DelayLossyBitErrors extends RDT3LossyBitErrors {
	
	private static double DELAYPB = 0.2;
	private static int DELAYLIMIT = 1000;
	
	public int delay () {
		
		int delay = 0;
		
		if (Math.random() <= DELAYPB) {
			delay = (int)(DELAYLIMIT * Math.random());
		}
		
		return delay;
	}
	
	public Datagram process (String name, Datagram datagram) {
		return (super.process(name,datagram));
	}
}
