package no.hvl.dat110.transport;

public interface ITransportProtocolEntity {

    /**
     * Invoked when the application layer has data to be sent by the transport layer
     * @param data The data to be sent by the transport layer
     */
    void rdt_send(byte[] data);

    /**
     * Invoked from transport layer to deliver data to the application layer
     * @param data The data to be delivered to the application layer
     */
    void deliver_data(byte[] data);

    /**
     * Invoked to send data using the network layer
     * @param segment The segment to be sent using the network layer
     */
    void udt_send(Segment segment);

    /**
     * Invoked when the network layer has a segment for the transport layer
     * @param segment The segment received from the network layer
     */
    void rdt_recv(Segment segment);

}
