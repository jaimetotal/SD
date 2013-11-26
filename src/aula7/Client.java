package aula7;

import java.net.InetAddress;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 05-11-2013
 * Time: 21:18
 * To change this template use File | Settings | File Templates.
 */
public class Client {

    private long responseTime;
    private InetAddress address;
    private final long serverTime = 999999999999999999l;
    private long timeDifference;
    private int port;

    public Client(long responseTime, InetAddress address, int port) {
        this.responseTime = responseTime;
        this.address = address;
        timeDifference = serverTime - responseTime;
        this.port = port;
    }

    public boolean isValid()
    {
        return true;
    }

    public boolean requiresHitHours()
    {
        return timeDifference > 0;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public InetAddress getAddress() {
        return address;
    }

    public long getServerTime() {
        return serverTime;
    }

    public long getTimeDifference() {
        return timeDifference;
    }

    public int getPort() {
        return port;
    }
}
