package no.hvl.dat110.network;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import no.hvl.dat110.common.Stopable;

public class Channel {

	protected LinkedBlockingQueue<Datagram> datagramqueue;
	protected String name;
	protected IAdversary observer;
	
	public Channel(String name, IAdversary observer) {
		this.name = name;
		datagramqueue = new LinkedBlockingQueue<Datagram>();
		this.observer = observer;
	}

	public void send(Datagram datagram) {

		try {
			
			System.out.print("[Network:"+name+ "] transmit: " + datagram.toString());
			
			datagram = observer.process(datagram);
						
			if (datagram != null) {
				datagramqueue.put(datagram);
			}
		
			
		} catch (InterruptedException ex) {

			System.out.println("Channel send " + ex.getMessage());
			ex.printStackTrace();
		}

	}

	public Datagram receive() {

		Datagram data = null;

		try {
			data = datagramqueue.poll(2, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {

			System.out.println("Channel receive " + ex.getMessage());
			ex.printStackTrace();

		}

		return data;
	}
}
