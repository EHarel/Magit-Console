package menu.action;

import dto.files.RepoFile;
import errors.exceptions.RepoNotSetException;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

import java.util.Collection;

public class ShowWorkingCopy implements DoesAction {
    @Override
    public void DoAction() {
        Collection<RepoFile> changedFiles = null;
        try {
            changedFiles = RepoAPI.getWorkingCopy();
            Utils.printFiles(changedFiles);
        } catch (RepoNotSetException e) {
            Utils.repoNotSetMsg();
        }

    }
}
