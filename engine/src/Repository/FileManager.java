package Repository;

import DataObjects.Commit;
import DataObjects.files.RepoFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileManager {

    /*****************************************************
     ******************** CONSTANTS **********************
     *****************************************************/
    public static final String MAGIT_DIR = "_magit";
    public static final String BRANCHES_DIR = "branches";
    public static final String OBJECTS_DIR = "objects";
    public static final String HEAD_FILE = "HEAD";

    public static boolean isValidPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            return false;
        }

        boolean res = false;
        try {
            Paths.get(path);
            res = true;
        } catch (InvalidPathException ex) {
            res = false;
        }

        return res;
    }

    public static void unzip(String zippedFilePath, String destinationPath) {
        try
        {
            zippedFilePath = checkAndAddZipSuffix(zippedFilePath);
            int BUFFER = 2048;
            File file = new File(zippedFilePath);
            ZipFile zip = new ZipFile(file);
            String newPath = destinationPath;
            Enumeration zipFileEntries = zip.entries();

            // Process each entry
            while (zipFileEntries.hasMoreElements())
            {
                // grab a zip file entry
                ZipEntry entry = (ZipEntry) zipFileEntries.nextElement();
                String currentEntry = entry.getName();
                File destFile = new File(newPath, currentEntry);

                if (!entry.isDirectory())
                {
                    BufferedInputStream is = new BufferedInputStream(zip
                            .getInputStream(entry));
                    int currentByte;
                    // establish buffer for writing file
                    byte data[] = new byte[BUFFER];

                    // write the current file to disk
                    FileOutputStream fos = new FileOutputStream(destFile);
                    BufferedOutputStream dest = new BufferedOutputStream(fos,
                            BUFFER);

                    // read and write until last byte is encountered
                    while ((currentByte = is.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, currentByte);
                    }
                    dest.flush();
                    dest.close();
                    is.close();
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function checks if the file path ends with ".zip", and adds the suffix if not.
     * @param filePath
     */
    private static String checkAndAddZipSuffix(String filePath) {
        int length = filePath.length();
        if (! filePath.substring(length - 4).equals(".zip")) {
            filePath = filePath + ".zip";
        }

        return filePath;
    }

    public static void zip(String zipPath, String fileName, String content) {
        try {
            FileOutputStream fout = new FileOutputStream(zipPath);
            ZipOutputStream zout = new ZipOutputStream(fout);
            ZipEntry zipEntry = new ZipEntry(fileName);
            zout.putNextEntry(zipEntry);

            byte[] data = content.getBytes(StandardCharsets.UTF_8);
            zout.write(data, 0, data.length);
            zout.closeEntry();
            zout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param repoPath
     * @return the name of the branch as saved in the HEAD file.
     */
    public static String getHeadBranchName(String repoPath) throws NoSuchFileException {
        // TODO: check path validity
        if (repoPath == null) return null;

        String headFilePath = FileManager.getHeadPath(repoPath);

        return getFileContent(headFilePath);
    }

    public static String getFileContent(String filePathStr) throws NoSuchFileException {
        if (filePathStr == null) return null;

        Path filePath = Paths.get(filePathStr);

        if ( ! Files.exists(filePath)) throw new NoSuchFileException(filePathStr);

        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append(System.lineSeparator()));
        } catch (IOException e) {
            // TODO handle exception
        }

        String fileContent = contentBuilder.toString();

        int length = fileContent.length();

        if (fileContent.endsWith(System.lineSeparator())) {
            fileContent = fileContent.substring(0, length - 1);
        }

        return fileContent;
    }

    /**
     * This file returns the SHA1 of the branch file, which is the last commit of the branch.
     *
     * @param mainDir
     * @param branchName
     */
    public static String getBranchSha1(String mainDir, String branchName) throws NoSuchFileException {
        String branchesPath = FileManager.getBranchesPath(mainDir);
        String branchPath = branchesPath + branchName;

        return getFileContent(branchPath);
    }

    public static void writeToFile(String filePath, String content, boolean append) {
        try {
            FileWriter writerObj = new FileWriter(filePath, append);
            writerObj.write(content);
            writerObj.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void deleteWC(String mainDirPath) {
        File dirFile = new File(mainDirPath);

        for (File file : dirFile.listFiles()) {
            if (file.isDirectory() && file.getName().equals(FileManager.MAGIT_DIR)) {
                continue;
            }

            purgeDirectory(file);
        }
    }

    /**
     * Deletes a file including its sub-files.
     * If the file sent is not a directory, the file itself is deleted.
     * If the file sent is a directory, all its subdirectories and files are deleted recursively.
     * @param file
     */
    private static void purgeDirectory(File file) {
        if (file.isDirectory()) {
            for (File childFile : file.listFiles()) {
                purgeDirectory(childFile);
            }

        }

        file.delete();
    }

    public static Commit getCommit(String mainDir, String commitSha1) {
        String objPath = FileManager.getObjectsPath(mainDir);
        String commitPath = objPath + commitSha1 + ".zip";
        String commitString = FileManager.readZipContent(commitPath);

        return new Commit(commitString);
    }

    /**
     * Returns the content of a single zipped file. returns null if path is invalid or file is a directory.
     */
    private static String readZipContent(String filePath) {
        if (filePath == null) return null;

        filePath = checkAndAddZipSuffix(filePath);

        String fileContent = null;

        // Read zipped commit string
        try {
            ZipFile zipFile = new ZipFile(filePath);

            ZipEntry zipEntry = zipFile.entries().nextElement();
            if (zipEntry == null) return null;

            InputStream stream = zipFile.getInputStream(zipEntry);

            Scanner scanner = new Scanner(stream).useDelimiter("\\A");
            fileContent = scanner.hasNext() ? scanner.next() : "";

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileContent;
    }

    /**
     * This method unfolds a whole commit in the working copy.
     * It does not delete the content of the working copy. That occurs elsewhere.
     * @param mainDirPath
     * @param branchCommit
     */
    public static void unfoldCommit(String mainDirPath, Commit branchCommit) {
        /*
        We don't need the whole object created for unfolding the commit,
        Just the name of the file, its SHA1 to find the zipped object, and its type.
         */
        String mainDirSha1 = branchCommit.getMainDirSha1();
        String objectsPath = FileManager.getObjectsPath(mainDirPath);
        unzipIntoDir(objectsPath, mainDirPath, mainDirSha1);
    }

    /**
     * This function receives the name of a zipped folder object, and the path to an existing folder of that name.
     * It recursively unzips all the relevant files into the
     * @param destDirPath Path to the directory where all files will be unzipped.
     * @param dirSha1 The name of the zipped folder (SHA1) that holds the contents of the directory.
     */
    private static void unzipIntoDir(String objectsPath, String destDirPath, String dirSha1) {
        String dirObjectPath = objectsPath + dirSha1;
        String mainDirString = FileManager.readZipContent(dirObjectPath);
        String[] dirFiles = mainDirString.split("\n");

        for (String fileString : dirFiles) {
            String[] stringFields = fileString.split(RepoFile.delimiter);

            /*
            FIXME: Quick and dirty!
            I'm hard-coding code that parses based on the documentation and how the string is created.
            Gotta find a better way that is much more maintainable and can handle changes.
             */
            String fileName = stringFields[0];
            String fileSha1 = stringFields[1];
            String fileType = stringFields[2];

            if (fileType.equals(RepoFile.FileType.BLOB.toString())) {
                // Unzip to current directory
                String filePath = objectsPath + fileSha1;
                FileManager.unzip(filePath, destDirPath);
            } else {
                String dirPath = destDirPath + File.separator + fileName;

                try {
                    Files.createDirectories(Paths.get(dirPath));
    //                new File(dirPath);
                    unzipIntoDir(objectsPath, dirPath, fileSha1);
                    // TODO: move try-catch area
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // TODO
    public static boolean isExistingRepo(String path) {
        String fullPath = appendToPath(path, FileManager.MAGIT_DIR);

        return Files.exists(Paths.get(fullPath));
    }

    public static String appendToPath(String path, String addition) {
        path = path.trim();
        char lastChar = path.charAt(path.length() - 1);
        if (lastChar != '/' && lastChar != '\\') {
            path = path + File.separator;
        }

        return path + addition;
    }

    public static void advanceHeadBranch(String mainDirPath, String commitSha1) throws NoSuchFileException {
        String headBranchName = FileManager.getHeadBranchName(mainDirPath);
        String filePath = FileManager.getBranchesPath(mainDirPath) + File.separator + headBranchName;
        FileManager.writeToFile(filePath, commitSha1, false);
    }



    /*****************************************************
                        BASIC PATH GETTERS
     *****************************************************/

    public static String getMagitPath(String mainDirPath) {
        return mainDirPath +
                File.separator +
                MAGIT_DIR +
                File.separator;
    }

    public static String getBranchesPath(String mainDirPath) {
        return getMagitPath(mainDirPath) +
                File.separator +
                BRANCHES_DIR +
                File.separator;
    }

    public static String getObjectsPath(String mainDirPath) {
        return getMagitPath(mainDirPath) +
                File.separator +
                OBJECTS_DIR +
                File.separator;
    }

    public static String getHeadPath(String mainDirPath) {
        return FileManager.getBranchesPath(mainDirPath) +
                File.separator +
                HEAD_FILE;
    }
}
