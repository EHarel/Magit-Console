package DataObjects;

public class FileData {
    public enum FileType {
        BLOB, FOLDER
    }

    public static final String delimeter = ",";

    private String name;
    private String id; // SHA1
    private FileType fileType;
    private String lastUpdater;
    private String lastUpdateDate;



    /*****************************************************
     * GETTERS AND SETTERS *
     ****************************************************/
    public String getName() { return name; }
    public String getId() { return id; }
    public String getLastUpdater() { return lastUpdater; }
    public String getLastUpdateDate() { return lastUpdateDate; }
    public FileType getFileType() { return fileType; }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLastUpdater(String lastUpdater) {
        this.lastUpdater = lastUpdater;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public String toString() {
        StringBuilder dataBuilder = new StringBuilder();

        dataBuilder.append(name);
        dataBuilder.append(delimeter);

        dataBuilder.append(id);
        dataBuilder.append(delimeter);

        dataBuilder.append(fileType);
        dataBuilder.append(delimeter);

        dataBuilder.append(lastUpdater);
        dataBuilder.append(delimeter);

        dataBuilder.append(lastUpdateDate);

        String data = dataBuilder.toString();

        return data;
    }
}
