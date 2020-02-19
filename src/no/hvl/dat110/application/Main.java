package no.hvl.dat110.application;

import no.hvl.dat110.network.Network;
import no.hvl.dat110.transport.*;
import no.hvl.dat110.transport.rdt1.Adversary;
import no.hvl.dat110.transport.rdt1.TransportReceiverRDT1;
import no.hvl.dat110.transport.rdt1.TransportSenderRDT1;

public class Main {

	public static void main(String[] args) {

		Network network = new Network(new Adversary());
		network.doRun();
		
		TransportSenderRDT1 tsender = new TransportSenderRDT1();
		tsender.register(network.getService(0)); 
		TransportReceiverRDT1 treceiver = new TransportReceiverRDT1();
		treceiver.register(network.getService(1));
		
		tsender.start();
		treceiver.start();
		
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
