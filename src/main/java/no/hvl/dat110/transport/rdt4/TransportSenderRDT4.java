package no.hvl.dat110.transport.rdt4;

import java.util.concurrent.LinkedBlockingQueue;

import no.hvl.dat110.transport.Segment;
import no.hvl.dat110.transport.rdt3.SegmentRDT3;
import no.hvl.dat110.network.NetworkService;
import no.hvl.dat110.transport.ITransportProtocolEntity;
import no.hvl.dat110.transport.TransportSender;

public class TransportSenderRDT4 extends TransportSender implements ITransportProtocolEntity {

		private LinkedBlockingQueue<SegmentRDT3> recvqueue;		
	    
	    // TODO
	    
		public TransportSenderRDT4(NetworkService ns) {
			super("TransportSender",ns);
			recvqueue = new LinkedBlockingQueue<SegmentRDT3>();
			
			// TODO
		}

		public void rdt_recv(Segment segment) {

			System.out.println("[Transport:Sender   ] rdt_recv: " + segment.toString());

			try {

				recvqueue.put((SegmentRDT3) segment);

			} catch (InterruptedException ex) {
				System.out.println("TransportSenderRDT4 thread " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		public void doProcess() {

			// TODO

		}

	}
