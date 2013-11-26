package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.Index;
import tp_continua.Peer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 18-11-2013
 * Time: 0:05
 * Student Number: 8090309
 */
public class QueryFiles implements Runnable {

    private ClientConnectionManager connectionManager;
    private Client client;

    public QueryFiles(ClientConnectionManager connectionManager, Client client) {
        this.connectionManager = connectionManager;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //TODO check nulls
            DatagramSocket responseSocket = connectionManager.getUDPSocket();
            connectionManager.sendMulticast(ConnectionManager.QUERY_FILES);
            //Receives ConnectionManager.QUERY_FILES_ACK + PORT SIZE + \n
            byte[] responseBuf = new byte[ConnectionManager.QUERY_FILES_ACK.length() + 5];
            DatagramPacket packet = new DatagramPacket(responseBuf, responseBuf.length);

            boolean timeOut = false;
            while (!timeOut) {
                try {
                    responseSocket.receive(packet);
                    String messageReceived = packet.getData().toString();
                    if (messageReceived.startsWith(ConnectionManager.QUERY_FILES_ACK)) {
                        int port = new Scanner(messageReceived).nextInt();
                        new Thread(new FetchQuery(new Peer(packet.getAddress(), port)));
                    }
                } catch (java.net.SocketTimeoutException ex) {
                    timeOut = true;
                }
            }
        } catch (Exception e) {
            //TODO revise null argument
            client.queryFailed(new QueryFailedEvent(null));
        }
    }


    //TODO check need for this
    private class FetchQuery implements Runnable {
        private Peer node;

        private FetchQuery(Peer node) {
            this.node = node;
        }

        @Override
        public void run() {
            //TODO CREATE GENERIC METHOD FOR THIS
            //TODO Get Socket
            try (ByteArrayInputStream stream = connectionManager.getInputStream(node, ConnectionManager.QUERY_FILES)) {
                ObjectInputStream objectInputStream = new ObjectInputStream(stream);
                Index index = (Index) objectInputStream.readObject();
                client.queryCompleted(new QueryCompletedEvent(index, node));
                //TODO catch close
                objectInputStream.close();
            } catch (IOException e) {
                //TODO Treat error as network issue?
                client.queryFailed(new QueryFailedEvent(node));
            } catch (ClassNotFoundException e) {
                //TODO Treat error as invalid data?
                client.queryFailed(new QueryFailedEvent(node));
            }
        }
    }
}