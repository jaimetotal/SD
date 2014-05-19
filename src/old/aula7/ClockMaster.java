package aula7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 05-11-2013
 * Time: 21:09
 * To change this template use File | Settings | File Templates.
 */
public class ClockMaster extends Thread {

    private List<Client> clients;

    public void run() {
        try {
            clients = new ArrayList<Client>();
            send();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private void send() throws IOException {
        String dString = "IT'S CLOCK TIME!!!";
        byte[] buf = dString.getBytes();



        MulticastSocket listenerSocket = new MulticastSocket(4546);
        DatagramSocket responseSocket = new DatagramSocket(4547);
        responseSocket.setSoTimeout(1000);
        InetAddress listenersGroup = InetAddress.getByName("230.0.0.1");



        DatagramPacket packet = new DatagramPacket(buf, buf.length, listenersGroup, 4546 );
        System.out.println("Send them all!");
        listenerSocket.send(packet);

        byte[] responseBuf = new byte[256];
        packet = new DatagramPacket(responseBuf, responseBuf.length);
        System.out.println("Waiting for response...");

        boolean timeOut = false;

        List<Thread> threadList = new ArrayList<Thread>();

        while (!timeOut)
        {
            try {
                responseSocket.receive(packet);
                System.out.println("Someone answered...");
                Thread thread = new Thread(new Response(clients, packet));
                threadList.add(thread);
                thread.start();
            }catch (Exception ex)
            {
                timeOut = true;
            }

        }

        for (Thread thread : threadList)
        {
            if(thread.isAlive())
            {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                }
            }
        }

        System.out.println("Updating all customers...");
        for (Client client : clients)
        {
            byte[] correction = String.valueOf(client.getTimeDifference()).getBytes();
            System.out.println("Updating " + client.getAddress() + ":" + client.getPort() + " with " + client.getTimeDifference());
            packet = new DatagramPacket(correction, correction.length, client.getAddress(), client.getPort());
            responseSocket.send(packet);
        }

        listenerSocket.close();
    }

    private class Response implements Runnable{

        private List<Client> clients;
        private DatagramPacket response;

        private Response(List<Client> clients, DatagramPacket response) {
            this.clients = clients;
            this.response = response;
        }

        @Override
        public void run() {
            String received = new String(response.getData(), 0, response.getLength());
            Client client = new Client(Long.parseLong(received), response.getAddress(), response.getPort());
            clients.add(client);
        }
    }
}
