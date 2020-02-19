package no.hvl.dat110.application;

import no.hvl.dat110.network.Network;
import no.hvl.dat110.network.models.PerfectChannelRDT1;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt1.TransportReceiverRDT1;
import no.hvl.dat110.transport.rdt1.TransportSenderRDT1;

public class Main {

	public static void main(String[] args) {

		// setup the network
		Network network = new Network(new PerfectChannelRDT1());
		network.doRun();
		
		// setup and start the transport protocol entities
		TransportSenderRDT1 tsender = new TransportSenderRDT1();
		tsender.register(network.getService(0)); // TODO move into constructor? 
		TransportReceiverRDT1 treceiver = new TransportReceiverRDT1();
		treceiver.register(network.getService(1)); // TODO move into constructor?
		
		tsender.start();
		treceiver.start();
		
		// setup and start the application level sender and receiver processes 
		SenderProcess sender = new SenderProcess(tsender);
		ReceiverProcess receiver = new ReceiverProcess(treceiver);
		
		sender.doRun();
		
		try {
			
			Thread.sleep(10000); // allow for reception of outstanding messages
			
			tsender.doStop();
			treceiver.doStop();
			
			network.doStop();
			
		} catch (InterruptedException ex) {

			System.out.println("Main thread " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
