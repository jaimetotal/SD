package tp_continua.server;

import tp_continua.Peer;

import java.util.EventObject;

/**
 * Event for when a network transmission has been received by a given protocol
 */
public class IncomingTransmissionEvent extends EventObject {

    public enum Protocol {
        TCP, UDP
    }

    private String message;
    private Protocol protocol;

    /**
     * @param source   Peer which open connection
     * @param message  Token received within the message
     * @param protocol Protocol used for transmission
     */
    public IncomingTransmissionEvent(Peer source, String message, Protocol protocol) {
        super(source);
        this.message = message;
        this.protocol = protocol;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public Peer getSource() {
        return (Peer) super.getSource();
    }

    public Protocol getProtocol() {
        return protocol;
    }
}
