package dto.files;

import utils.SharedUtils;

import java.util.Collection;
import java.util.LinkedList;

public class Folder extends RepoFile {
    private final Collection<RepoFile> files;

    public Folder() {
        this(null);
    }

    public Folder(MetaData metaData) {
        if (metaData != null) {
            this.metaData = metaData;
        } else {
            this.metaData = new MetaData();
            this.metaData.setFileType(FileType.FOLDER);
        }

        files = new LinkedList<>();
    }


    public Collection<RepoFile> getFiles() {
        return files;
    }

    public void addFile(RepoFile file) {
        files.add(file);
        String data = this.getContent();
        this.id = SharedUtils.getSha1(data);
    }

    @Override
    public String getContent() {
        return getChildrenData();
    }

    private String getChildrenData() {
        StringBuilder data = new StringBuilder();

        for (RepoFile rf : files) {
            String fileStr = rf.getMetaDataContent();
            data.append(fileStr).append("\n");
        }

        return data.toString();
    }

//    // TODO: change to something more maintainable
//    public Folder(String folderString) {
//        this.fileType = FileType.FOLDER;
//        files = new LinkedList<>();
//
//        String[] filesString = folderString.split(System.lineSeparator());
//
//        for (String fileString : filesString) {
//            String[] fileFields = fileString.split(delimiter);
//            FileType fileType = fileFields[2].equals(FileType.FOLDER.toString()) ? FileType.FOLDER : FileType.BLOB;
//            RepoFile repoFile;
//
//            if (fileType == FileType.FOLDER) {
//                repoFile = new Folder();
//            } else {
//                repoFile = new Blob();
//            }
//
//            repoFile.setName(fileFields[0]);
//            repoFile.setId(fileFields[1]);
//            repoFile.setLastUpdater(fileFields[3]);
//            repoFile.setLastUpdateDate(fileFields[4]);
//        }
//    }
}
