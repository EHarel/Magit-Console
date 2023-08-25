package DataObjects;

// import org.apache.commons.codec.digest.DigestUtils;
import utils.Utils;

import java.io.Serializable;

public class Commit implements Serializable {
    private static final long serialVersionUID = 0; // 31-Jul-2023

    private String mainDirSha1;
    private String mainAncestorSha1;
    private String secondaryAncestorSha1;
    private String msg;
    private String date;
    private String creator;

    public Commit() {}

    // FIXME: this is quick and dirty! Very hard to maintain. Improve.
    /**
     * Creates a commit from the string used to describe a Commit saved as a file.
     * @param fileStr
     */
    public Commit(String fileStr) {
        /*
        Note: The problem with this function is that it's very reliant on the toString function.
        Changing one will change the other.
        This seems very risky. Might want a better way to do it.
         */

        String[] fields = fileStr.split(FileData.delimeter);
        int i = 0;
        this.mainDirSha1 = fields[i++];
        this.mainAncestorSha1 = fields[i++];
        this.secondaryAncestorSha1 = fields[i++];
        this.msg = fields[i++];
        this.date = fields[i++];
        this.creator = fields[i++];
    }

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
        String sha1Sstr = Utils.getSha1(commitStr);

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
