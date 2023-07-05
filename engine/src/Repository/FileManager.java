package Repository;

import DataObjects.Commit;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class FileManager {
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
     * This file returns the SHA1 of the branch file, which is the last commit of the branch.
     * @param mainDir
     * @param branchName
     */
    public static String getBranchSha1(String mainDir, String branchName) {
        String branchesPath = mainDir + "/magit/branches/" + branchName;

        Path filePath = Paths.get(branchesPath);
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(filePath, StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s).append("\n"));
        } catch (IOException e) {
            //handle exception
        }

        String fileContent = contentBuilder.toString();

        return fileContent;
    }

    public static Commit getCommit(String mainDir, String commitSha1) {
        Commit commit = null;

        String commitPath = mainDir + "/.magit/objects/" + commitSha1;


        // Read zipped commit string
        try {
            ZipFile zipFile = new ZipFile(commitPath);
            if (zipFile == null) return null;

            ZipEntry zipEntry = zipFile.getEntry(commitSha1);
            if (zipEntry == null) return null;

            String zipStr = zipEntry.toString();
            InputStream stream = zipFile.getInputStream(zipEntry);

            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(commitPath))) {

            stream.forEach(System.out::println);

        } catch (IOException e) {
            e.printStackTrace();
        }





        return commit;
    }
}
