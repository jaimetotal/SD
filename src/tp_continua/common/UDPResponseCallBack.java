package tp_continua.common;

/**
 * Interface to treat an UDP response
 */
public interface UDPResponseCallBack {
    public void messageReceived(String messageReceived, Peer peer);
}
