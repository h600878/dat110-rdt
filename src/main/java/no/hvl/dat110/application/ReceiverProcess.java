package no.hvl.dat110.application;

import java.util.ArrayList;

import no.hvl.dat110.transport.TransportReceiver;

public class ReceiverProcess {

	private ArrayList<byte[]> datarecv;

	public ReceiverProcess(TransportReceiver transport) {
		transport.register(this);
		datarecv = new ArrayList<byte[]>();

	}

	public ArrayList<byte[]> getDatarecv() {
		return datarecv;
	}

	public void deliver_data(byte[] data) {

		datarecv.add(data);

		String message = new String(data);

		System.out.println("[App:ReceiverProcess] dlv_data: " + "[" + message + "]");
	}
}
