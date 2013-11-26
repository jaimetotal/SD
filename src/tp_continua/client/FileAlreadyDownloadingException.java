package tp_continua.client;

import tp_continua.File;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 18-11-2013
 * Time: 21:48
 * Student Number: 8090309
 */
public class FileAlreadyDownloadingException extends Throwable {

    public FileAlreadyDownloadingException(File file) {
        super(String.format("File %s is already being downloaded.", file.getFileName()));
    }
}
