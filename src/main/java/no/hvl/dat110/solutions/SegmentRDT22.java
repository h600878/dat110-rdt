package no.hvl.dat110.solutions;

import no.hvl.dat110.transport.rdt2.SegmentType;
import no.hvl.dat110.transport.rdt21.SegmentRDT21;

public class SegmentRDT22 extends SegmentRDT21 {

	public SegmentRDT22() {
		super();
	}
	
	public SegmentRDT22(byte[] data, int seqnr) {
		super(data,seqnr);
	}
	
	public SegmentRDT22(SegmentType type) {
		super(type);
	}
	
	public SegmentRDT22(SegmentType type, int seqnr) {
		super(type,seqnr);
	}
	
	@Override
	public SegmentRDT22 clone() {

		SegmentRDT22 segment = new SegmentRDT22();

		if (this.data != null) {
			segment.data = this.data.clone();
		}

		segment.type = this.type;
		segment.checksum = this.checksum;
		segment.seqnr = this.seqnr;
		
		return segment;
	}
	
	@Override
	public String toString() {
		
		String str = super.toString();
		
		if (type == SegmentType.ACK) {
			str = str + "[" + seqnr + "]";
		}
		
		return str;
	}
}
