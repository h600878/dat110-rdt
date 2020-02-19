package no.hvl.dat110.network;

public class Network {

	private Channel srchannel, rschannel;
	private NetworkService srns, rsns;

	public Network(IChannelModel chanmodel) {

		srchannel = new Channel("S --> R", chanmodel);
		rschannel = new Channel("R --> S", chanmodel);

		srns = new NetworkService("Channel S->R", rschannel, srchannel);
		rsns = new NetworkService("Channel R->S", srchannel, rschannel);
	}

	public NetworkService getSenderService() {
		return srns;
	}

	public NetworkService getReceiverService() {
		return rsns;
	}

	public void doRun() {

		System.out.println("Network starting");

		srns.start();
		rsns.start();
	}

	public void doStop() {

		try {

			srns.doStop();
			srns.join();

			rsns.doStop();
			srns.join();

		} catch (InterruptedException ex) {

			System.out.println("Network thread " + ex.getMessage());
			ex.printStackTrace();
		}
		System.out.println("Network stopping");
	}

}
