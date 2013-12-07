package tp_continua;

import tp_continua.client.*;
import tp_continua.server.Server;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 03-12-2013
 * Time: 19:47
 * Student Number: 8090309
 */
public class Main implements DownloadCompletedEvent.DownloadCompletedEventListener, DownloadFailedEvent.DownloadFailedEventListener, QueryCompletedEvent.QueryCompletedEventListener, QueryFailedEvent.QueryFailedEventListener {

    private Client client;
    private Server server;
    private FileSystem fs;

    public Main() throws IOException {
        fs = new FileSystem(".\\filesamples");
        server = new Server(fs);
        server.start();
        client = new Client(fs);
        client.start();
        client.addDownloadCompletedEventListener(this);
        client.addDownloadFailedEventListener(this);
        client.addQueryCompletedEventListener(this);
        client.addQueryFailedEventListener(this);
        client.queryNetwork();
        displayMenu();
    }

    public static void main(String[] args) throws IOException {
        new Main();
    }

    /**
     * Displays menu
     */
    private void displayMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("1) Consult Files \n2) Download File \n3) Check downloads in progress\n4)Refresh available files\ns) Exit");
            System.out.print("Selection: ");
            Scanner in = new Scanner(System.in);
            String next = in.next();
            switch (next) {
                case "1":
                    listFiles();
                    break;
                case "2":
                    downloadFiles();
                    break;
                case "3":
                    checkDownloadProgress();
                    break;
                case "4":
                    queryNetwork();
                    break;
                case "s":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    private void queryNetwork() {
        System.out.println("Querying network...");
        client.queryNetwork();
    }

    private void checkDownloadProgress() {
        Enumeration<PeerFile> enumerator = client.getDownloadingFiles();
        while (enumerator.hasMoreElements()) {
            PeerFile file = enumerator.nextElement();
            System.out.println(String.format("File %s.", file));
        }
    }

    private void listFiles() {
        if (fs.listFiles().isEmpty()) {
            System.out.println("There isn't any available to download.");
            return;
        }
        System.out.println("The files on network:\na) Download all");

        int n = 1;
        for (PeerFile f : fs.remoteFiles()) {
            System.out.println(String.format("%d) %s", n, f.getFileName()));
            n++;
        }
        System.out.println("s) Return");
    }


    private void downloadFiles() {
        listFiles();
        Scanner in = new Scanner(System.in);
        String next = in.next();
        switch (next) {
            case "a":
                for (PeerFile file : fs.remoteFiles()) {
                    try {
                        client.getFile(file);
                    } catch (FileAlreadyDownloadingException e) {
                        System.out.println(String.format("SYSTEM: File %s is already downloading...", file));
                    }
                }
                break;

            case "s":
                break;
            default:
                Integer value = Integer.getInteger(next);

                if (value == null || value <= 0 || fs.listFiles().size() < value) {
                    System.out.println("Invalid option.");
                } else {
                    try {
                        client.getFile((PeerFile) fs.remoteFiles().toArray()[value - 1]);
                    } catch (FileAlreadyDownloadingException e) {
                        System.out.println("SYSTEM: File is already downloading...");
                    }
                }
                break;
        }

    }

    @Override
    public void downloadCompleted(DownloadCompletedEvent e) {
        System.out.println(String.format("SYSTEM: Download completed for %s.", e.getSource()));
    }

    @Override
    public void downloadFailed(DownloadFailedEvent e) {
        System.out.println(String.format("SYSTEM: Download failed for %s.", e.getSource()));
    }

    @Override
    public void queryCompleted(QueryCompletedEvent e) {
        System.out.println("SYSTEM: File querying finished.");
    }

    @Override
    public void queryFailed(QueryFailedEvent e) {
        if (e.getSource() == null) {
            System.out.println("SYSTEM: Failed to obtain files to download. Try again later.");
        }
    }
}
