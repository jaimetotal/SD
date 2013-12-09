package tp_continua.server;

import tp_continua.common.ConnectionManager;
import tp_continua.common.FileSystem;
import tp_continua.common.InternalLogger;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * Server responsible for listening incoming transmissions and answer their request
 */
public class Server extends Thread implements IncomingTCPTransmissionEvent.IncomingTCPTransmissionEventListener, IncomingUDPTransmissionEvent.IncomingUDPTransmissionEventListener {

    private ExecutorService executorService;
    private ServerConnectionManager connectionManager;
    private FileSystem fileSystem;
    private BlockingQueue<Runnable> threadQueue;
    private static final short THREADQUEUE_MAXSIZE = 10;
    private static final short THREADPOOL_MAXSIZE = 5;
    private InternalLogger logger;

    public Server(FileSystem fileSystem) throws IOException {
        this.logger = InternalLogger.getLogger(this.getClass());
        threadQueue = new ArrayBlockingQueue<Runnable>(THREADQUEUE_MAXSIZE);
        this.executorService = new ThreadPoolExecutor(THREADPOOL_MAXSIZE, THREADPOOL_MAXSIZE, Long.MAX_VALUE, TimeUnit.DAYS, threadQueue);
        this.fileSystem = fileSystem;
        connectionManager = new ServerConnectionManager(this, executorService);
    }

    @Override
    public void incomingTCPTransmission(IncomingTCPTransmissionEvent event) {
        logger.info("Incoming TCP transmission with message %s.", event.getMessage());
        if (event.getMessage().equals(ConnectionManager.QUERY_FILES)) {
            executorService.submit(new ListFiles(event.getSocket(), fileSystem.getIndex()));
        } else if (event.getMessage().equals(ConnectionManager.DOWNLOAD_FILE)) {
            executorService.submit(new UploadFile(event.getSocket(), fileSystem, event.getStdIn()));
        } else {
            try {
                event.getSocket().close();
            } catch (IOException e) {
            }
        }
    }

    @Override
    public void incomingUDPTransmission(IncomingUDPTransmissionEvent event) {
        logger.info("Incoming UDP transmission with message %s.", event.getMessage());
        if (event.getMessage().startsWith(ConnectionManager.QUERY_FILES)) {
            int port = new Scanner(event.getMessage().substring(ConnectionManager.QUERY_FILES.length())).nextInt();
            event.getSource().setPort(port);
            executorService.submit(new Acknowledge(connectionManager, event.getSource()));
        } else {
            logger.warn("UDP Message %s ignored.", event.getMessage());
        }
    }
}