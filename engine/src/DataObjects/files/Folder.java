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

    // TODO: change to something more maintainable
    public Folder(String folderString) {
        this.fileType = FileType.FOLDER;
        files = new LinkedList<>();

        String[] filesString = folderString.split("\n");

        for (String fileString : filesString) {
            String[] fileFields = fileString.split(RepoFile.delimiter);
            FileType fileType = fileFields[2].equals(FileType.FOLDER.toString()) ? FileType.FOLDER : FileType.BLOB;
            RepoFile repoFile;

            if (fileType == FileType.FOLDER) {
                repoFile = new Folder();
            } else {
                repoFile = new Blob();
            }

            repoFile.setName(fileFields[0]);
            repoFile.setId(fileFields[1]);
            repoFile.setLastUpdater(fileFields[3]);
            repoFile.setLastUpdateDate(fileFields[4]);
        }
    }
}
