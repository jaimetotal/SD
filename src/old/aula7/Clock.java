package aula7;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 05-11-2013
 * Time: 21:10
 * To change this template use File | Settings | File Templates.
 */
public class Clock  {

    public static void main(String[] args) throws IOException {

        MulticastSocket groupSocket = new MulticastSocket(4546);
        DatagramSocket replySocket = new DatagramSocket();
        InetAddress groupAddress = InetAddress.getByName("230.0.0.1");
        groupSocket.joinGroup(groupAddress);

        DatagramPacket packet;

        // get a few quotes
        for (int i = 0; i < 5; i++) {

            byte[] buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            System.out.println("Client standing by");
            groupSocket.receive(packet);
            String serverCommand = new String(packet.getData(), 0, packet.getLength());

            System.out.println("Received: " + serverCommand);

            buf = "500000".getBytes();
            replySocket = new DatagramSocket(4548);
            packet = new DatagramPacket(buf, buf.length, packet.getAddress(), 4547);
            replySocket.send(packet);
            System.out.println("Sent current time. Waiting for correction.");

            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);

            replySocket.receive(packet);

            String timeResponse = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received correction of " + timeResponse);
        }

        groupSocket.leaveGroup(groupAddress);
        groupSocket.close();
        replySocket.close();
    }

}
