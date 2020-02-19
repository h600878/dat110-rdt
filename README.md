### Protocols Techniques for Reliable Data Transfer

For doing some practical experiments with implementation of transport layer protocols for reliable data transfer (rdt), we will use the framework which can be found in the Eclipse project at:

The main goal of this exercise is to start becoming familiar with this framework.

##### Exercise 1 - Cloning the RDT Framework

Start by importing the RDT framework and testing projects into your IDE (Eclipse):

https://github.com/selabhvl/dat110-rdt

https://github.com/selabhvl/dat110-rdt-testing

and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.transport` contains the classes that implements the transport protocols for reliable data transfer of segments. The `TransportSender` and `TransportReceiver` classes in this package, implements the reliable data transfer over a perfectly reliable channel (rdt1.0) as described on pages 236-237 in the networking book.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel we can simulate reliable and unreliable networks and experiments with different transport protocol implementations.

- `no.hvl.dat110.transport.rdt2` implements the rdt2.0 transport protocol for reliable data transfer over a channel with bit errors as described on pages 237-239 in the networking book

The rdt-testing projects implements some JUnit-tests for testing the transport protocols. The basic requirement is that the receiver must receive all data from the sender and in correct order.

##### Exercise 2 - Basic experiments

Perform the following experiments

1. Run the main-method in `Main.java` where a sender process sends three messages to the receiver. Try to understand the output generated in the console and where it is generated from.

2. Run the `TestRDT1.java` which tests the rdt1.0 transport protocol over a perfect channel.

3. Run the `TestRDT2.java` which tests the rdt2.0 transport protocol over a perfect channel.

4. Run the `TestRDT21.java` which test the rdt2.0 transport protocol over a channel with bit-errors.

#### Exercise 2 - Handling Corrupt ACK/NAK segments

The `Adversary2.java` class implements a network adversary that at random sets the checksum in the transmitted segment to `0` thereby simulating a transmission errors. This in will case the `doProcess()` methods in `TransportReceiverRDT2.java` to detect that the receive segment has a checksum errors and therefore send a NAK segment.

Methods for calculating and checking checksums can be found in the `Segment.java` class.

As a checksum of `0` is the correct checksum for ACK and NAK segments (see Segment-constructors), then this effectively mean that only DATA segments can have transmission errors.

##### Exercise 2.1

Modify the implementation of the `Segment.java` class such that the correct checksum simulating a transmission error is set to 1. This means that also ACK/NAK can have checksum errors.

##### Exercise 2.2

Augment the implementation of the sender side in the rdt2.0 transport protocol in `TransportSenderRDT2.java` such that the sender checks for any transmission errors on ACK and NAK segments.

What could/should the sender do in case a corrupt ACK / NAK segment is received?

Implement you proposed solution and use the `TestRDT21.java` test test the solution. You can modify the probability of transmission errors by adjusting the value `CORRUPTPB` in `TestRDT2BitErrors.java`

#### Exercise 1 - Reliable Data Transfer Framework

Exercises 2 and 3 below is concerned with implementation of transport layer protocols based on the RDT Java framework that has been introduced and discussed in the lectures on the transport layer.

To undertake the following two exercises it is assumed that you have read pages 234-245 in the networking book. You may also want to review the lecture notes from the transport layer I and transport layer II lectures, in particular the parts that cover the RDT implementation framework.

Make sure that you have pulled the most recent version of the framework available as an Eclipse-project at:

https://github.com/selabhvl/dat110public/tree/master/week6/rdt

If you have not already done so earlier, then start by importing the project into your IDE (Eclipse) and make sure that the classes and the tests compiles.

The project is organised into the following packages

- `no.hvl.dat110.application` implements a sender process that sends messages to a receiver process using the underlying transport protocol implementation.

- `no.hvl.dat110.network` implements a simulated network that can connect a sender and a receiver by means of two channels (one in each direction). By changing the type of channel and the associated adversary, we can simulate reliable and unreliable networks and experiment with different transport protocol implementations.

- `no.hvl.dat110.transport` contains the base classes for implementing transport protocols for reliable data transfer of segments and defines the basic primitives of `rdt_send`, `deliver_data`, `udt_send`, and `rdt_recv`.

- `no.hvl.dat110.transport.rdt1` implements the RDT1.0 protocol for reliable data transfer over a perfectly reliable channel as described on pages 236-237 in the networking book.

- `no.hvl.dat110.trasport.tests` implements some JUnit-tests for testing the transport protocols. The basic correctness criteria is that the receiver must receive all data from the sender and in correct order.

#### Exercise 2 - RDT 2.2 Implementation

The  `no.hvl.dat110.transport.rdt2` package  implements the RDT 2.1 transport protocol for reliable data transfer over a channel with bit errors as described on pages 237-239 in the networking book. The implementation of the sender and receiver are in the classes `TransportSenderRDT21.java` and `TransportReceiverRDT21.java` as presented at the lectures.

The transport receiver of RDT2.1 uses a NAK (negative acknowledgement) to signal to the sender that a corrupt data segment has been received. As described on page 242 in the networking book, then it is also possible to replace the use of NAKs with the use of an ACK (acknowledgement) in combination with a sequence number.

Modify the RDT 2.1 implementation (i.e., the classes `TransportSenderRDT21.java` and `TransportReceiverRDT21.java`) to become an RDT2.2 implementation as described on page 242 in the networking book.

The test `TestRDT21Adversary21.java` can be used to test your protocol implementation.

#### Exercise 3 - Reliable Transport and Overtaking

The  `no.hvl.dat110.transport.rdt2` package implements the RDT 3.0 transport protocol for reliable data transfer over a channel with bit errors and loss of segments as described on pages 242-245 in the networking book.

##### Task-1: Study the RDT 3.0 Implementation

The implementation of the sender in `TransportSenderRDT3.java` uses a timer to implement timeouts, and thereby recover from possible loss of data and acknowledgements. Study the implementation of the transport sender and transport receiver in order to understand how it implements retransmission and the state machines shown in Figures 3.15 and 3.16.

The test in `TestRDT3Adversary3.java` can be used to run the implementation.  

##### Task-2: Implementation of the RDT 4.0 Protocol

The RDT3.0 protocol cannot recover from overtaking of segments, i.e., that segments may arrive in a different order from which they were sent.

The `no.hvl.dat110.transport.rdt4` package contains templates for implementing a RDT 4.0 protocol that can recover from overtaking of segments. The protocol is to replace the alternating bit sequence number of RDT3.0 with an increasing sequence number and operates as follows:

- The sender keeps sending the same data segment (having the same sequence number) until an acknowledgement with a sequence number which is one higher is received. This should include retransmission similar to RDT 3.0. This means that the sender interprets a sequence number received in an acknowledge which is one higher that its current sequence number as indication that the receiver has received the data segment with the current sequence number.

- The receiver keeps an internal sequence number indicating the data segment expected next. If a data segment with the expected sequence number is received, then the internal sequence number is incremented and a corresponding acknowledgement segment is sent back to the sender. If the receiver receives a data segment with the wrong (non-expected) sequence number, then the receiver replies with an acknowledgement segment containing the sequence number of the data segment expected next.

The test in `TestRDT4Adversary4.java` can be used to test and run the protocol implementation.

##### Task-3: Variant of the RDT4.0 Protocol

Modify the implementation of the receiver side of the RDT4.0 protocol in Task-2 such that an acknowledgement is only sent if the data segment with the expected sequence number is received. Is the protocol still correct?
