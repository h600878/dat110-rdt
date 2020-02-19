package no.hvl.dat110.network;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Channel {

	protected LinkedBlockingQueue<Datagram> datagramqueue;
	protected String name;
	protected IChannelModel adversary;

	public Channel(String name, IChannelModel adversary) {
		this.name = name;
		datagramqueue = new LinkedBlockingQueue<Datagram>();
		this.adversary = adversary;
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
	private String getNetwork() {
		return name;
	}

	class DelayDatagram extends TimerTask {

		private Datagram datagram;

		public DelayDatagram(Datagram datagram) {
			this.datagram = datagram;
		}

		public void run() {

			try {
				
				System.out.println("[Network:" + getNetwork() + "] delayed arrival: " + datagram.toString());
				
				datagramqueue.put(datagram);
				
			} catch (InterruptedException ex) {

				System.out.println("Delay channel send " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}

	public void send(Datagram datagram) {

		System.out.print("[Network: " + name + "   ] transmit: " + datagram.toString());

		datagram = adversary.process(datagram);

		if (datagram != null) {

			int delay = adversary.delay();
			
			if (delay > 0) {

				Timer timer = new Timer();
				timer.schedule(new DelayDatagram(datagram), delay);

			} else {

				try {

					datagramqueue.put(datagram);

				} catch (InterruptedException ex) {

					System.out.println("Delay channel send " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		} 
		
		System.out.println();
	}
}
