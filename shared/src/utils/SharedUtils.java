package utils;

import java.io.File;
import java.io.IOException;

public abstract class SharedUtils {
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
