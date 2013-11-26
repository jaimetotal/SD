package tp_continua;


import java.util.Collection;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: AntónioJaime
 * Date: 12-11-2013
 * Time: 21:07
 * Student Number: 8090309
 */
public class FileSystem {

    private CopyOnWriteArrayList<File> files;

    public FileSystem() {
        files = new CopyOnWriteArrayList<File>();
    }

    public File getFileByName(String fileName)
    {
        for (File file : files)
        {
            if(file.getFileName().equals(fileName))
            {
                return file;
            }
        }
        return null;
    }

    public void addFile(File file)
    {
        if(!files.contains(file))
        {
            files.add(file);
        }
    }

    public Collection<File> listFiles()
    {
        //TODO check readonly collections
        return files;
    }

    public Index getIndex()
    {
        return new Index(files);
    }

    public void addRemoteIndex(Peer source, Index index) {
        for(String fileName : index.getFilesName())
        {
            File file = new File(fileName, null, source);
            if(this.files.contains(file))
            {
                this.addFile(file);
            }
        }
    }

    //TODO check if required
/*
    public void updateFile(File remoteFile)
    {
        for (File localFile : files)
        {
            if(localFile.equals(remoteFile))
            {
                files.remove(localFile);
                files.add(remoteFile);
                return;
            }
        }
        files.add(remoteFile);
    }*/
}
