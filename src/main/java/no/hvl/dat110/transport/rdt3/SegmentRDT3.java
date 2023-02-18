package no.hvl.dat110.transport.rdt3;

import no.hvl.dat110.transport.rdt2.SegmentType;
import no.hvl.dat110.transport.rdt21.SegmentRDT21;

public class SegmentRDT3 extends SegmentRDT21 {
	
	public SegmentRDT3() {
		
	}
	
	public SegmentRDT3(byte[] data, int seqnr) {
		super(data,seqnr);
	}
	
	public SegmentRDT3(SegmentType type, int seqnr) {
		super(type);
		super.seqnr = seqnr;
	}
	
	public int getSeqnr () {
		return this.seqnr;
	}
	
	@Override
	public SegmentRDT3 clone() {

		SegmentRDT3 segment = new SegmentRDT3();

		if (this.data != null) {
			segment.data = this.data.clone();
		}

		segment.type = this.type;
		segment.checksum = this.checksum;
		segment.seqnr = this.seqnr;
		
		return segment;
	}
	
	public String toString() {
		
		String str = super.toString();
		
		if (type == SegmentType.ACK) {
			str = str + "[" + seqnr + "]";
		}
		
		return str;
	}
}
