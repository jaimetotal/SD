package tp_continua.client;

import tp_continua.common.FileSystem;
import tp_continua.common.InternalLogger;
import tp_continua.common.PeerFile;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.*;

/**
 * Client responsible for querying files available in network and downloading them
 */
public class Client extends Thread implements DownloadCompletedEvent.DownloadCompletedEventListener, DownloadFailedEvent.DownloadFailedEventListener, DownloadManagerDispatcher, QueryDispatcher, QueryFailedEvent.QueryFailedEventListener, QueryCompletedEvent.QueryCompletedEventListener, AuthResponseEvent.AuthResponseEventListener, AuthFailedEvent.AuthFailedEventListener {

    private final String authUser;
    private final String authPass;
    private ExecutorService executorService;
    private FileSystem fileSystem;
    private List<DownloadCompletedEvent.DownloadCompletedEventListener> downloadCompletedEventListener;
    private List<DownloadFailedEvent.DownloadFailedEventListener> downloadFailedEventListeners;
    private List<QueryCompletedEvent.QueryCompletedEventListener> queryCompletedEventListener;
    private List<QueryFailedEvent.QueryFailedEventListener> queryFailedEventListeners;
    private List<AuthResponseEvent.AuthResponseEventListener> authResponseEventListeners;
    private List<AuthFailedEvent.AuthFailedEventListener> authFailedEventListeners;
    private BlockingQueue<Runnable> threadQueue;
    private ConcurrentHashMap<PeerFile, Future<?>> filesDownloading;

    private static final short THREADQUEUE_MAXSIZE = 10;
    private static final short THREADPOOL_MAXSIZE = 20;
    private static final short MULTICAST_TIMEOUT = 10;
    private InternalLogger logger;

    /**
     * Construtor of Client
     *
     * @param fileSystem Filesystem used to save files and receive logical files location
     * @param helloUser
     * @param helloPass
     */
    public Client(FileSystem fileSystem, String helloUser, String helloPass) {
        this.authUser = helloUser;
        this.authPass = helloPass;
        logger = InternalLogger.getLogger(this.getClass());
        threadQueue = new ArrayBlockingQueue<Runnable>(THREADQUEUE_MAXSIZE);
        this.executorService = new ThreadPoolExecutor(THREADPOOL_MAXSIZE, THREADPOOL_MAXSIZE, Long.MAX_VALUE, TimeUnit.DAYS, threadQueue);
        this.fileSystem = fileSystem;
        this.downloadCompletedEventListener = new ArrayList<DownloadCompletedEvent.DownloadCompletedEventListener>();
        this.downloadFailedEventListeners = new ArrayList<DownloadFailedEvent.DownloadFailedEventListener>();
        this.queryCompletedEventListener = new ArrayList<QueryCompletedEvent.QueryCompletedEventListener>();
        this.queryFailedEventListeners = new ArrayList<QueryFailedEvent.QueryFailedEventListener>();
        this.filesDownloading = new ConcurrentHashMap<PeerFile, Future<?>>();
        this.authResponseEventListeners = new ArrayList<>();
        this.authFailedEventListeners = new ArrayList<>();
    }

    /**
     * Sends a multicast to network to obtain index of available files to download from other peers
     * until reaches the timeout
     */
    public void queryNetwork() {
        logger.info("Echoing through the network with a timeout of %d seconds.", MULTICAST_TIMEOUT);
        //Creates local executor due to awaiting termination for this Runnable only
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new QueryFiles(this));
        try {
            executor.awaitTermination(MULTICAST_TIMEOUT, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.info("File querying failed.");
            executor.shutdown();
        }
    }

    /**
     * Downloads a given file from a remote node
     *
     * @param peerFile File to be download
     * @throws FileAlreadyDownloadingException
     *          Fired when this file is already being downloaded
     */
    public void getFile(PeerFile peerFile) throws FileAlreadyDownloadingException {
        logger.info("Trying to download file %s.", peerFile);
        if (filesDownloading.containsKey(peerFile)) {
            logger.info("Ops. Already downloading it", peerFile);
            throw new FileAlreadyDownloadingException(peerFile);
        }
        logger.info("File download %s in queue.", peerFile);
        Future<?> future = executorService.submit(new Download(peerFile, this));
        filesDownloading.put(peerFile, future);
    }

    /**
     * Cancels download process from the server
     *
     * @param peerFile File's download to be cancelled
     */
    public void cancelFile(PeerFile peerFile) {
        logger.info("Trying to cancel file %s.", peerFile);
        if (filesDownloading.containsKey(peerFile)) {
            filesDownloading.get(peerFile).cancel(true);
            filesDownloading.remove(peerFile);
            logger.info("File cancelling %s successfully.", peerFile);
        } else {
            logger.warn("File cancelling %s failed.", peerFile);
        }
    }

    /**
     * Event listener for when a download is completed
     *
     * @param e Event which fire
     */
    @Override
    public void downloadCompleted(DownloadCompletedEvent e) {
        logger.info("Download completed for %s.", e.getSource());
        filesDownloading.remove(e.getSource());
        for (DownloadCompletedEvent.DownloadCompletedEventListener listener : downloadCompletedEventListener) {
            listener.downloadCompleted(e);
        }
    }

    /**
     * Event listener for when a download fails to download
     *
     * @param e Event which fire
     */
    @Override
    public void downloadFailed(DownloadFailedEvent e) {
        logger.info("Download failed for %s.", e.getSource());
        filesDownloading.remove(e.getSource());
        for (DownloadFailedEvent.DownloadFailedEventListener listener : downloadFailedEventListeners) {
            listener.downloadFailed(e);
        }
    }

    /**
     * Event listener for when a new query has been received
     *
     * @param e Event which fire
     */
    @Override
    public void queryCompleted(QueryCompletedEvent e) {
        logger.info("QueryCompletedEvent received from %s.", e.getSource());
        fileSystem.addRemoteIndex(e.getSource(), e.getIndex());
        for (QueryCompletedEvent.QueryCompletedEventListener listener : queryCompletedEventListener) {
            listener.queryCompleted(e);
        }
    }

    /**
     * Event for when the network's query has failed or the download of a peer's index failed
     *
     * @param e Event which fire
     */
    @Override
    public void queryFailed(QueryFailedEvent e) {
        logger.info("QueryFailedEvent received from %s", e.getSource());
        for (QueryFailedEvent.QueryFailedEventListener listener : queryFailedEventListeners) {
            listener.queryFailed(e);
        }
    }

    @Override
    public void addDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener) {
        this.downloadCompletedEventListener.add(listener);
    }

    @Override
    public void removeDownloadCompletedEventListener(DownloadCompletedEvent.DownloadCompletedEventListener listener) {
        this.downloadCompletedEventListener.remove(listener);
    }

    @Override
    public void addDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener) {
        this.downloadFailedEventListeners.add(listener);
    }

    @Override
    public void removeDownloadFailedEventListener(DownloadFailedEvent.DownloadFailedEventListener listener) {
        this.downloadFailedEventListeners.remove(listener);
    }

    @Override
    public void addQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener) {
        this.queryCompletedEventListener.add(listener);
    }

    @Override
    public void removeQueryCompletedEventListener(QueryCompletedEvent.QueryCompletedEventListener listener) {
        this.queryCompletedEventListener.remove(listener);
    }

    @Override
    public void addQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener) {
        this.queryFailedEventListeners.add(listener);
    }

    @Override
    public void removeQueryFailedEventListener(QueryFailedEvent.QueryFailedEventListener listener) {
        this.queryFailedEventListeners.remove(listener);
    }

    public Enumeration<PeerFile> getDownloadingFiles() {
        return filesDownloading.keys();
    }

    public void authenticate() {
        executorService.submit(new AuthRequest(this, authUser, authPass));
    }

    @Override
    public void authFailed(AuthFailedEvent e) {
        for (AuthFailedEvent.AuthFailedEventListener listener : authFailedEventListeners) {
            authFailed(e);
        }
    }

    @Override
    public void authResponse(AuthResponseEvent e) {
        for (AuthResponseEvent.AuthResponseEventListener listener : authResponseEventListeners) {
            listener.authResponse(e);
        }
    }
}
