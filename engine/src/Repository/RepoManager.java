package Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RepoManager {
    public static final int ERROR_INVALID_PATH = 1;
    public static final int ERROR_EXISTING_REPO = 2;
    public static final int ERROR_UNKNOWN = 3;

    private static final String MAGIT_DIR = ".magit";

    /**
     * @param path
     * @return (*) 0 - repo created.
     * (*) 1 - invalid path.
     * (*) 2 - repo exists.
     */
    public static int createRepo(String path) {
        int resCode = 0;

        if (!isValidPath(path)) {
            return ERROR_INVALID_PATH;
        }

        if (isExistingRepo(path)) {
            return ERROR_EXISTING_REPO;
        }

        // Create directory
        resCode = createDirectories(path);

        return resCode;
    }

    // TODO
    private static boolean isValidPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }

        boolean res;
        try {
            Paths.get(path);
            res = true;
        } catch (Exception e) {
            res = false;
        }

        return res;
    }

    // TODO
    private static boolean isExistingRepo(String path) {
        String fullPath = appendToPath(path, MAGIT_DIR);

        return Files.exists(Paths.get(fullPath));
    }

    private static String appendToPath(String path, String addition) {
        path = path.trim();
        char lastChar = path.charAt(path.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            path = path + "\\";
        }

        String fullPath = path + addition;

        return fullPath;
    }

    private static int createDirectories(String path) {
        int resCode;

        String fullPath = appendToPath(path, MAGIT_DIR);
        try {
            Files.createDirectories(Paths.get(fullPath));

            String objPath = appendToPath(fullPath, "objects");
            Files.createDirectories(Paths.get(objPath));

            String branchesPath = appendToPath(fullPath, "branches");
            Files.createDirectories(Paths.get(branchesPath));

            String headPath = appendToPath(branchesPath, "HEAD");
            Files.createFile(Paths.get(headPath));

            resCode = 0;
        } catch (IOException e) {
            e.printStackTrace();
            resCode = ERROR_UNKNOWN;
        }

        return resCode;
    }
}
