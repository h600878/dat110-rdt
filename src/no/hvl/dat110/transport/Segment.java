package no.hvl.dat110.transport;

import java.util.Arrays;

import no.hvl.dat110.transport.rdt2.SegmentType;

public class Segment {

	protected byte[] data;
	
	public Segment () {
		super();
	}
	
	public Segment (byte[] data) {
	
		this.data = data;
	}
	
	public Segment clone () {
	
		Segment segment = new Segment();
		
		if (this.data != null) {
		 segment.data = this.data.clone();
		} 
		
		return segment;
	}
	
	public byte[] getData() {
		return data;
	}
	
	public String toString() {
		
		String str = "";
		
		if (data != null) {
			str = "[" + (new String(data)) + "]"; 
					
		}

		return str;
	}
	
	
}
