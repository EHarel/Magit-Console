package DataObjects;

import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;

public class Commit {
    private String mainDirSha1;
    private String mainAncestorSha1;
    private String secondaryAncestorSha1;
    private String msg;
    private String date;
    private String creator;

    public String getMainDirSha1() {
        return mainDirSha1;
    }

    public void setMainDirSha1(String mainDirSha1) {
        this.mainDirSha1 = mainDirSha1;
    }

    public String getMainAncestorSha1() {
        return mainAncestorSha1;
    }

    public void setMainAncestorSha1(String mainAncestorSha1) {
        this.mainAncestorSha1 = mainAncestorSha1;
    }

    public String getSecondaryAncestorSha1() {
        return secondaryAncestorSha1;
    }

    public void setSecondaryAncestorSha1(String secondaryAncestorSha1) {
        this.secondaryAncestorSha1 = secondaryAncestorSha1;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getSha1() {
        String commitStr = this.toString();
        String sha1Sstr = DigestUtils.sha1Hex(commitStr);

        return sha1Sstr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(mainDirSha1);
        sb.append(FileData.delimeter);

        sb.append(mainAncestorSha1);
        sb.append(FileData.delimeter);

        sb.append(secondaryAncestorSha1);
        sb.append(FileData.delimeter);

        sb.append(msg);
        sb.append(FileData.delimeter);

        sb.append(date);
        sb.append(FileData.delimeter);

        sb.append(creator);

        String str = sb.toString();

        return str;
    }
}
