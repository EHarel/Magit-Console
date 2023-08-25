package menu.action;

import errors.exceptions.ExistingRepoException;
import errors.exceptions.InvalidPathException;
import menu.DoesAction;
import magit.RepoAPI;
import menu.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class CreateRepo implements DoesAction {
    @Override
    public void DoAction() {
        String msg = "Enter path of new repository: ";
        boolean validInput = false;
        String pathStr = null;
        while (! validInput) {
            pathStr = Utils.getInput(msg, false);
            if (pathStr == null) return;

            validInput = Utils.isValidFilePath(pathStr);
            if (! validInput) {
                System.out.println("Invalid path. Try again.");
            }
        }

        String resMsg;
        try {
            RepoAPI.createRepo(pathStr);
            resMsg = "Repository created.";
        } catch (ExistingRepoException e) {
            resMsg = "ERROR! Repository already exists.";
        } catch (IOException e) {
            resMsg = "UNKNOWN ERROR!";
        } catch (InvalidPathException e) {
            resMsg = "ERROR! Invalid path.";
        }

        System.out.println();
        System.out.println(resMsg);
    }
}
