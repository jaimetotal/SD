package tp_continua.client;

import tp_continua.ConnectionManager;
import tp_continua.File;
import tp_continua.FileSystem;

import java.util.*;
import java.util.concurrent.*;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class Client extends Thread implements DownloadCompletedEvent.DownloadCompletedEventListener, DownloadFailedEvent.DownloadFailedEventListener, DownloadManagerDispatcher, QueryDispatcher, QueryFailedEvent.QueryFailedEventListener, QueryCompletedEvent.QueryCompletedEventListener {

    private ExecutorService executorService;
    private ConnectionManager connectionManager;
    private FileSystem fileSystem;
    private List<DownloadCompletedEvent.DownloadCompletedEventListener> downloadCompletedEventListener;
    private List<DownloadFailedEvent.DownloadFailedEventListener> downloadFailedEventListeners;
    private List<QueryCompletedEvent.QueryCompletedEventListener> queryCompletedEventListener;
    private List<QueryFailedEvent.QueryFailedEventListener> queryFailedEventListeners;
    private BlockingQueue<Runnable> threadQueue;
    private ConcurrentHashMap<File, Future<?>> filesDownloading;


    public Client(FileSystem fileSystem) {
        //TODO review sizes
        threadQueue = new ArrayBlockingQueue<Runnable>(Integer.MAX_VALUE);
        this.executorService = new ThreadPoolExecutor(5, 5, Long.MAX_VALUE, TimeUnit.DAYS, threadQueue);
        this.fileSystem = fileSystem;
        this.downloadCompletedEventListener = new ArrayList<DownloadCompletedEvent.DownloadCompletedEventListener>();
        this.downloadFailedEventListeners = new ArrayList<DownloadFailedEvent.DownloadFailedEventListener>();
        this.queryCompletedEventListener = new ArrayList<QueryCompletedEvent.QueryCompletedEventListener>();
        this.queryFailedEventListeners = new ArrayList<QueryFailedEvent.QueryFailedEventListener>();
        this.filesDownloading = new ConcurrentHashMap<File, Future<?>>();
    }

    public void queryNetwork() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new QueryFiles(connectionManager, this));
        //TODO Revise this due to being blocked
        try {
            executor.awaitTermination(10, TimeUnit.SECONDS); // Timeout of 10 minutes.
        } catch (InterruptedException e) {
            executor.shutdown();
        }
    }

    public void getFile(File file) throws FileAlreadyDownloadingException {
        if (filesDownloading.containsKey(file)) {
            throw new FileAlreadyDownloadingException(file);
        }
        Future<?> future = executorService.submit(new Download(connectionManager, file, this));
        filesDownloading.put(file, future);
    }

    public void cancelFile(File file) {
        if (filesDownloading.containsKey(file)) {
            filesDownloading.get(file).cancel(true);
            filesDownloading.remove(file);
        }
    }

    public Collection<File> listFiles() {
        return fileSystem.listFiles();
    }

    @Override
    public void downloadCompleted(DownloadCompletedEvent e) {
        filesDownloading.remove(e.getSource());
        fileSystem.addFile(e.getSource());
        for (DownloadCompletedEvent.DownloadCompletedEventListener listener : downloadCompletedEventListener) {
            listener.downloadCompleted(e);
        }
    }

    @Override
    public void downloadFailed(DownloadFailedEvent e) {
        filesDownloading.remove(e.getSource());
        for (DownloadFailedEvent.DownloadFailedEventListener listener : downloadFailedEventListeners) {
            listener.downloadFailed(e);
        }
    }

    @Override
    public void queryCompleted(QueryCompletedEvent e) {
        fileSystem.addRemoteIndex(e.getSource(), e.getIndex());
        for (QueryCompletedEvent.QueryCompletedEventListener listener : queryCompletedEventListener) {
            listener.queryCompleted(e);
        }
    }

    @Override
    public void queryFailed(QueryFailedEvent e) {
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

}
