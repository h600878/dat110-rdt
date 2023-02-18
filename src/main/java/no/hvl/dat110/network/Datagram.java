package no.hvl.dat110.network;

import no.hvl.dat110.transport.Segment;

public class Datagram {

	private Segment segment;
	
	public Datagram(Segment segment) {
		this.segment = segment;
	}
	
	public Segment getSegment() {
		return this.segment;
	}
	
	public String toString() {
		return segment.toString();
	}
}
