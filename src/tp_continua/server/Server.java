package tp_continua.server;

import tp_continua.ConnectionManager;
import tp_continua.FileSystem;

import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class Server extends Thread implements IncomingTCPTransmissionEvent.IncomingTCPTransmissionEventListener, IncomingUDPTransmissionEvent.IncomingUDPTransmissionEventListener {

    private ExecutorService executorService;
    private ServerConnectionManager connectionManager;
    private FileSystem fileSystem;
    private BlockingQueue<Runnable> threadQueue;
    private static final short THREADQUEUE_MAXSIZE = 10;

    public Server(FileSystem fileSystem) {
        //TODO review sizes
        threadQueue = new ArrayBlockingQueue<Runnable>(THREADQUEUE_MAXSIZE);
        this.executorService = new ThreadPoolExecutor(5, 5, Long.MAX_VALUE, TimeUnit.DAYS, threadQueue);
        this.fileSystem = fileSystem;
        connectionManager = new ServerConnectionManager(this, executorService);
    }

    @Override
    public void incomingTCPTransmission(IncomingTCPTransmissionEvent event) {
        if (event.getMessage().equals(ConnectionManager.QUERY_FILES)) {
            executorService.submit(new ListFiles(event.getSocket(), fileSystem.getIndex()));
        } else if (event.getMessage().equals(ConnectionManager.DOWNLOAD_FILE)) {
            executorService.submit(new UploadFile(event.getSocket(), fileSystem));
        } else {
            //TODO report error?
        }
    }

    @Override
    public void incomingUDPTransmission(IncomingUDPTransmissionEvent event) {
        if (event.getMessage().equals(ConnectionManager.QUERY_FILES)) {
            executorService.submit(new Acknowledge(connectionManager, event.getSource()));
        } else {
            //TODO report error?
        }
    }
}