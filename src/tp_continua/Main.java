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
        downloadDemo();
    }

    private static void downloadDemo() {
        queryFiles();
        try {
            System.out.println("Sleeping for 5 secs.");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        downloadFile();
    }

    private static void queryFiles() {
        client.queryNetwork();
    }

    private static void downloadFile() {
        try {
            client.getFile((PeerFile) fs.listFiles().toArray()[5]);
        } catch (FileAlreadyDownloadingException e) {
            System.out.println("Já querias n?");
        }
    }

}
