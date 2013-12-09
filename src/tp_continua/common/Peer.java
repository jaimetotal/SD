package tp_continua.common;

import java.net.InetAddress;

/**
 * Representation of a node in the network
 */
public class Peer {

    private String address;
    private int port;

    public Peer(InetAddress address, int port) {
        this.address = address.getHostName();
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Peer)) return false;

        Peer peer = (Peer) o;

        if (port != peer.port) return false;
        if (!address.equals(peer.address)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + port;
        return result;
    }

    @Override
    public String toString() {
        return "Peer{" +
                "address='" + address + '\'' +
                ", port=" + port +
                '}';
    }
}
