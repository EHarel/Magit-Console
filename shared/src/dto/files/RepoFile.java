package dto.files;

/**
 * Named RepoFile instead of just File to differentiate it from java.nio.file.Files.
 */
public class RepoFile {
    public static final String delimiter = ",";

    public enum FileType {
        BLOB, FOLDER
    }

    public enum ChangeType {
        NEW, MODIFIED, DELETED, UNDEFINED
    }

    public RepoFile() {}

    protected MetaData metaData;


    protected String name;
    protected String id; // SHA1
    protected RepoFile.FileType fileType;
    protected String lastUpdater;
    protected String lastUpdateDate;

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    protected ChangeType changeType;


    /*********************************************************
     * GETTERS AND SETTERS
     *********************************************************/
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public FileType getFileType() { return fileType; }
    public void setFileType(FileType fileType) { this.fileType = fileType; }

    public String getLastUpdater() { return lastUpdater; }
    public void setLastUpdater(String lastUpdater) { this.lastUpdater = lastUpdater; }

    public String getLastUpdateDate() { return lastUpdateDate; }
    public void setLastUpdateDate(String lastUpdateDate) { this.lastUpdateDate = lastUpdateDate; }

    public String getContent() {
        return null;
    }

    /**
     *
     * @return A string of the data as it will be saved into the file system.
     * Currently of the format:
     *      [name],[sha1],[type],[user],[date]
     */
    public String getMetaDataContent() {
        String data =
                name +
                        delimiter +
                        id +
                        delimiter +
                        fileType +
                        delimiter +
                        lastUpdater +
                        delimiter +
                        lastUpdateDate;

        return data;
    }
}
