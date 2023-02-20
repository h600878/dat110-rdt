package no.hvl.dat110.transport.rdt2;

import no.hvl.dat110.transport.Segment;

public class SegmentRDT2 extends Segment {

    protected SegmentType type;
    protected byte checksum;

    public SegmentRDT2() {

    }

    public SegmentRDT2(byte[] data) {
        super(data);
        this.type = SegmentType.DATA;
        this.checksum = calcChecksum();
    }

    public SegmentRDT2(SegmentType type) {
        super();
        this.type = type;
        this.checksum = 0; // 0 is the correct checksum on ACK and NAK
    }

    @Override
    public SegmentRDT2 clone() {

        SegmentRDT2 segment = new SegmentRDT2();

        if (this.data != null) {
            segment.data = this.data.clone();
        }

        segment.type = this.type;
        segment.checksum = this.checksum;

        return segment;
    }

    public boolean isData() {
        return type == SegmentType.DATA;
    }

    public SegmentType getType() {
        return type;
    }

    @Override
    public String toString() {

        String str = type.toString();

        str = str + super.toString();

        str = str + "[" + (String.format("%8s", Integer.toBinaryString(checksum & 0xFF).replace(' ', '0'))) + "]";

        return str;

    }

    public byte calcChecksum() {

        byte sum = 0;

        if (this.data != null) {

            for (byte datum : this.data) {
                sum = (byte) (sum + datum);
            }
        }

        return sum;
    }

    public boolean isCorrect() {
        return (calcChecksum() == this.checksum);
    }

    // used by channel to simulate transmission error
    public void setChecksum(byte checksum) {
        this.checksum = checksum;
    }

    public byte getChecksum() {
        return this.checksum;
    }
}
