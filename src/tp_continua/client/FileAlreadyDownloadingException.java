package tp_continua.client;

import tp_continua.PeerFile;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 18-11-2013
 * Time: 21:48
 * Student Number: 8090309
 */
public class FileAlreadyDownloadingException extends Throwable {

    public FileAlreadyDownloadingException(PeerFile peerFile) {
        super(String.format("PeerFile %s is already being downloaded.", peerFile.getFileName()));
    }
}
