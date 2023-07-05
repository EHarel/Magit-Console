package DataObjects.files;

import utils.Utils;

import java.util.Collection;
import java.util.LinkedList;

public class Folder extends RepoFile {
    private Collection<RepoFile> files;

    public Folder() {
        this.fileType = FileType.FOLDER;
        files = new LinkedList<>();
    }

    public Collection<RepoFile> getFiles() {
        return files;
    }

    public void addFile(RepoFile file) {
        files.add(file);
        String data = this.getContent();
        String sha1 = Utils.getSha1(data);
        this.id = sha1;
    }

    @Override
    public String getContent() {
        String data = getChildrenData();

        return data;
    }

    private String getChildrenData() {
        String data = "";

        for (RepoFile rf : files) {
            String fileStr = rf.getMetaData();
            data = data + fileStr + "\n";
        }

        return data;
    }
}
