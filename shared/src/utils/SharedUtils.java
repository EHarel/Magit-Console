package utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;

public abstract class SharedUtils {
    public static String getSha1(String data) {
        return DigestUtils.sha1Hex(data);
    }
    public static boolean isValidFilePath(String path) {
        File f = new File(path);

        try {
            f.getCanonicalPath();
        } catch (IOException e) {
            return false;
        }

        return true;
    }
}
