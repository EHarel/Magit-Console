package DataObjects.files;

/**
 * Named RepoFile instead of just File to differentiate it from java.nio.file.Files.
 */
public abstract class RepoFile {
    public enum FileType {
        BLOB, FOLDER
    }

    public static final String delimiter = ",";

    protected String name;
    protected String id; // SHA1
    protected RepoFile.FileType fileType;
    protected String lastUpdater;
    protected String lastUpdateDate;


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

    public abstract String getContent();

    public String getMetaData() {
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
