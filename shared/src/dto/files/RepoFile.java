package dto.files;

/**
 * Named RepoFile instead of just File to differentiate it from java.nio.file.Files.
 */
public abstract class RepoFile {
    public static final String delimiter = ",";

    public enum FileType {
        BLOB, FOLDER
    }

    public enum ChangeType {
        New, Modified, Deleted, Unchanged
    }

    public RepoFile() {}

    protected MetaData metaData;
    protected ChangeType changeType;
    protected String fullPath;


    /*********************************************************
     * GETTERS AND SETTERS
     *********************************************************/

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }




    public abstract String getContent();

    public void setName(String newName) {
        this.metaData.setName(newName);
    }

    public String getName() {
        return this.metaData.getName();
    }

    public FileType getFileType() {
        return this.metaData.getFileType();
    }

    /**
     *
     * @return A string of the data as it will be saved into the file system.
     * Current format is:
     *
     *      [name],[sha1],[type],[user],[date]
     */
    public String getMetaDataContent() {
        return metaData.toString();
    }
}
