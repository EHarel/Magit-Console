package dto.files;

import org.apache.commons.codec.digest.DigestUtils;

public class Blob extends RepoFile {
    private String content;

    public Blob() {
        this(null, null);
    }

    public Blob(String content, MetaData metaData) {
        this.content = content;
        this.fileType = FileType.BLOB;
    }


    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        this.id = DigestUtils.sha1Hex(this.content);
    }
}
