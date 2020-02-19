package no.hvl.dat110.network;

public class Network {

	private Channel[] channels;
	private NetworkService[] networkservices;
	
	public Network (IAdversary observer) {
		
		channels = new Channel[2];
		
		channels[0] = new DelayChannel("Channel R>S",observer);
		channels[1] = new DelayChannel("Channel S>R",observer);
		
		networkservices = new NetworkService[2];
		
		networkservices[0] = new NetworkService("Network service 0",channels[0],channels[1]);
		networkservices[1] = new NetworkService("Network service 1",channels[1],channels[0]);
	}
	
	public NetworkService getService(int i) {
		return networkservices[i];
	}
	
	public void doRun() {
		
		System.out.println("Network starting");
				
		networkservices[0].start();
		networkservices[1].start();
	}
	
	public void doStop() {
	
		try {
			
		networkservices[0].doStop();
		networkservices[0].join();
		
		networkservices[1].doStop();
		networkservices[1].join();
		
		} catch (InterruptedException ex) {

		System.out.println("Main thread " + ex.getMessage());
		ex.printStackTrace();
	}
		System.out.println("Network stopping");
	}
	
}
