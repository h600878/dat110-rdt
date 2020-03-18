package no.hvl.dat110.transport.rdt3;

import java.util.Timer;
import java.util.TimerTask;

public class RetransmissionTimer {

	private boolean timeout;
	private Timer timer;

	private static int TIMEOUT = 1000;
	
	public RetransmissionTimer() {
		timeout = false;
	}

	// method used to check whether a timeout has occurred
	public boolean timeout() {
		return timeout;
	}

	public void stop() {
		timer.cancel();
		timeout = false;
	}

	public void start() {
		timer = new Timer();
		timer.schedule(new TimeOutTask(), TIMEOUT);
		timeout = false;
	}

	class TimeOutTask extends TimerTask {

		public void run() {
			System.out.println("[Transport:Sender   ] TIMEOUT");
			timer.cancel(); // unclear if actually needed or handled by schedule
			timeout = true;
		}
	}

}
