package tp_continua.server;

import tp_continua.Peer;

import java.util.EventListener;

/**
 * Event for when a UDP transmission has been received
 */
public class IncomingUDPTransmissionEvent extends IncomingTransmissionEvent {

    /**
     * @param source  Peer which open connection
     * @param message Token received within the message
     */
    public IncomingUDPTransmissionEvent(Peer source, String message) {
        super(source, message, Protocol.UDP);
    }

    public interface IncomingUDPTransmissionEventListener extends EventListener {
        void incomingUDPTransmission(IncomingUDPTransmissionEvent event);
    }
}
