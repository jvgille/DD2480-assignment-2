package group22;

import java.io.*;
import java.util.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.apache.commons.io.FileUtils;

/*
* A utility class that takes care of all JGit functionality; such as cloning the repository
* so that the CI seriver can compile the code and test it.
*/
public final class GitHandler{
    /*
    * @param branch: the name of the branch that is to be cloned
    * cloneRepo will clone the repository (at a specific branch) and put it in a data-folder
    * where the code can then be compiled and run by the CI server.
    */
    public static void cloneRepo(PushPayload p) throws GitAPIException,IOException {

        FileUtils.deleteDirectory(new File(ContinuousIntegrationServer.REPO_PATH));
        Git git = Git.cloneRepository()
                .setURI(p.repoURL)
                .setDirectory(new File(ContinuousIntegrationServer.REPO_PATH))
                .setBranchesToClone(Arrays.asList(p.ref))
                .setBranch(p.ref)
                .call();
      }

}
