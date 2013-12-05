package tp_continua.client;

import tp_continua.PeerFile;

/**
 * Exception to report when a file's download is already in progress and another of the same was requested
 */
public class FileAlreadyDownloadingException extends Throwable {

    public FileAlreadyDownloadingException(PeerFile peerFile) {
        super(String.format("PeerFile %s is already being downloaded.", peerFile.getFileName()));
    }
}
