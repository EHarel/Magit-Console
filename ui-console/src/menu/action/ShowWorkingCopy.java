package menu.action;

import dto.files.RepoFile;
import magit.RepoAPI;
import menu.DoesAction;
import menu.utils.Utils;

import java.util.Collection;

public class ShowWorkingCopy implements DoesAction {
    @Override
    public void DoAction() {
        Collection<RepoFile> changedFiles = RepoAPI.getWorkingCopy();

        Utils.printFiles(changedFiles);
    }
}
