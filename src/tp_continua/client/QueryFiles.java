package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.Index;
import tp_continua.Peer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 18-11-2013
 * Time: 0:05
 * Student Number: 8090309
 */
public class QueryFiles implements Runnable {

    private ConnectionManager connectionManager;
    private Client client;

    public QueryFiles(ConnectionManager connectionManager, Client client) {
        this.connectionManager = connectionManager;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            //TODO Receive socket from the ConnectionManager
            DatagramSocket responseSocket = new DatagramSocket(4547);
            //TODO Revise timeout
            responseSocket.setSoTimeout(1000);
            //TODO Revise buffer sizee
            byte[] responseBuf = new byte[256];
            DatagramPacket packet = new DatagramPacket(responseBuf, responseBuf.length);

            boolean timeOut = false;
            while (!timeOut) {
                try {
                    responseSocket.receive(packet);
                    //TODO Define message
                    if(packet.getData().toString().equals("QUERYFILES_OK_1989"))
                    {
                        new Thread(new FetchQuery(new Peer(packet.getAddress(), packet.getPort())));
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
                try(Socket socket = connectionManager.getSocketToServer(node))
                {
                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    Index index = (Index) objectInputStream.readObject();
                    socket.close();
                    client.queryCompleted(new QueryCompletedEvent(index, node));
                }
                catch (IOException e) {
                //TODO Treat error as network issue?
                client.queryFailed(new QueryFailedEvent(node));
            } catch (ClassNotFoundException e) {
                //TODO Treat error as invalid data?
                client.queryFailed(new QueryFailedEvent(node));
            }
        }
    }
}