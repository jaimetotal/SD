package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.Peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 19-11-2013
 * Time: 0:21
 * Student Number: 8090309
 */
public class ServerConnectionManager extends ConnectionManager {

    private boolean shutdown;
    private ExecutorService executorService;
    private Server server;

    public ServerConnectionManager(Server server, ExecutorService executorService) {
        this.server = server;
        this.executorService = executorService;
        this.executorService.submit(new IncomingTCP());
        this.executorService.submit(new IncomingUDP());
    }

    private class IncomingTCP implements Runnable {
        @Override
        public void run() {
            do {
                try {
                    ServerSocket socketServer = new ServerSocket(ConnectionManager.SERVER_TCP_PORT);
                    //Waits for a client to connect and fires the event. The event will be responsible to close socket
                    Socket incomingSocket = socketServer.accept();
                    Peer client = new Peer(incomingSocket.getInetAddress(), incomingSocket.getPort());
                    //TODO Catch whatever
                    BufferedReader stdIn = new BufferedReader(new BufferedReader(null));
                    String message = stdIn.readLine();
                    stdIn.close();

                    IncomingTCPTransmissionEvent event = new IncomingTCPTransmissionEvent(client, message, incomingSocket);
                    server.incomingTCPTransmission(event);
                    //TODO Fires event if Message is supposed to be accepted?
                } catch (IOException e) {
                    e.printStackTrace();
                    //TODO Treat exception
                }
            } while (!shutdown);
            //TODO Close socket
        }
    }

    private class IncomingUDP implements Runnable {
        @Override
        public void run() {
            while (!shutdown) {
                try (MulticastSocket socket = getMulticastListenerSocket()) {
                    //TODO Test what happens incoming when passes 256 bytes
                    byte[] buf = new byte[256];
                    DatagramPacket packet = new DatagramPacket(buf, buf.length);
                    //waits for input
                    socket.receive(packet);
                    //parses input
                    String received = new String(packet.getData(), 0, packet.getLength());
                    //fires incoming transmission event
                    Peer client = new Peer(packet.getAddress(), packet.getPort());
                    IncomingUDPTransmissionEvent event = new IncomingUDPTransmissionEvent(client, received);
                    server.incomingUDPTransmission(event);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    //TODO Close sockets & alike
                }
            }
        }
    }

    public void shutDown() {
        this.shutdown = true;
    }
}
