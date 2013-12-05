package tp_continua;

import tp_continua.client.Client;
import tp_continua.server.Server;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 03-12-2013
 * Time: 19:47
 * Student Number: 8090309
 */
public class Main {

    private static Client client;
    private static Server server;

    public static void main(String[] args) {
        FileSystem fs = new FileSystem(".\\filesamples");
        client = new Client(fs);
        client.start();
        server = new Server(fs);
        server.start();
        boolean exit = false;
        do {
            try {
                System.in.read();
            } catch (IOException e) {

            }
        } while (!exit);
    }

}
