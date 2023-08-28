package dto.files;


public class MetaData {
    private String name;
    private String id; // SHA1
    private RepoFile.FileType fileType;
    private String lastUpdater;
    private String lastUpdateDate;

    public MetaData() {}

    public MetaData(String dataString) {
        // TODO: throw exceptions

        String[] data = dataString.split(RepoFile.delimiter);
        int i = 0;
        name = data[i++];
        id = data[i++];
        fileType = RepoFile.FileType.valueOf(data[i++]);
        lastUpdater = data[i++];
        lastUpdateDate = data[i++];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RepoFile.FileType getFileType() {
        return fileType;
    }

    public void setFileType(RepoFile.FileType fileType) {
        this.fileType = fileType;
    }

    public String getLastUpdater() {
        return lastUpdater;
    }

    public void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
}
