package no.hvl.dat110.network;

public interface IChannelModel {

    /**
     * Millseconds to delay packet
     */
    int delay();

    /**
     * Callback when transmitting a datagram
     */
    Datagram process(String str, Datagram datagram);

}
