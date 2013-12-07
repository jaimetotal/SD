package tp_continua;

import tp_continua.client.Client;
import tp_continua.client.FileAlreadyDownloadingException;
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
    private static FileSystem fs;

    public static void main(String[] args) throws IOException {
        fs = new FileSystem(".\\filesamples");
        server = new Server(fs);
        server.start();
        client = new Client(fs);
        client.start();
        queryFiles();
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }

    }

    private static void queryFiles() {
        client.queryNetwork();
    }

    private static void downloadFile() {
        try {
            client.getFile((PeerFile) fs.listFiles().toArray()[0]);
        } catch (FileAlreadyDownloadingException e) {
            System.out.println("Já querias n?");
        }
    }

}
