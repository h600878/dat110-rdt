package no.hvl.dat110.network;

import java.util.Timer;
import java.util.TimerTask;

public class DelayChannel extends Channel {

	public DelayChannel(String name, IAdversary adversary) {
		super(name, adversary);
	}

	private String getNetwork() {
		return super.name;
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

	@Override
	public void send(Datagram datagram) {

		System.out.print("[Network:" + super.name + "] transmit: " + datagram.toString());

		datagram = observer.process(datagram);

		if (datagram != null) {

			IAdversary adversary = observer;

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
