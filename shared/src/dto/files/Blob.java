package dto.files;

import org.apache.commons.codec.digest.DigestUtils;
import utils.SharedUtils;

public class Blob extends RepoFile {
    private String content;

    public Blob() {
        this(null, null);
    }

    public Blob(String content, MetaData metaData) {
        this.content = content;
        if (metaData == null) {
            metaData = new MetaData();
        }

        this.metaData = metaData;
        this.metaData.setFileType(FileType.BLOB);
    }


    @Override
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;

//        this.id = DigestUtils.sha1Hex(this.content);
        this.metaData.setId(SharedUtils.getSha1(content));
    }
}
