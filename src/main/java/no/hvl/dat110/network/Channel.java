package no.hvl.dat110.network;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Channel {

	private LinkedBlockingQueue<Datagram> datagramqueue;
	private String name;
	private IChannelModel chanmodel;

	public Channel(String name, IChannelModel chanmodel) {
		this.name = name;
		datagramqueue = new LinkedBlockingQueue<Datagram>();
		this.chanmodel = chanmodel;
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

	class DelayDatagram extends TimerTask {

		private Datagram datagram;
        private int delay;
        
		public DelayDatagram(Datagram datagram,int delay) {
			this.datagram = datagram;
			this.delay = delay;
		}

		public void run() {

			try {

				System.out.println("[Network:" + name + "o   ]" + String.format("%9s"," delay(" + delay + "):") + datagram.toString());

				datagramqueue.put(datagram);

			} catch (InterruptedException ex) {

				System.out.println("Delay channel run " + ex.getMessage());
				ex.printStackTrace();
			}

		}
	}

	public void transmit(Datagram datagram) {

		if (datagram != null) {
			datagram = chanmodel.process(name, datagram);
		}

		if (datagram != null) {

			int delay = chanmodel.delay();

			if (delay > 0) {

				Timer timer = new Timer();
				timer.schedule(new DelayDatagram(datagram,delay), delay);

			} else {

				try {

					datagramqueue.put(datagram);

				} catch (InterruptedException ex) {

					System.out.println("Delay channel send " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
}
