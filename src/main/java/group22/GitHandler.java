package group22;

import java.io.*;
import java.util.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.apache.commons.io.FileUtils;

/*
* A utility class that takes care of all JGit functionality; such as cloning the repository
* so that the CI seriver can compile the code and test it.
*/
public final class GitHandler{
    private static String path = "data/repo";
    private static String url = "https://github.com/jvgille/DD2480-assignment-2.git";
    
    /*
    * @param branch: the name of the branch that is to be cloned
    * cloneRepo will clone the repository (at a specific branch) and put it in a data-folder
    * where the code can then be compiled and run by the CI server.
    */
    public static void cloneRepo(String branch) throws GitAPIException,IOException {

        FileUtils.deleteDirectory(new File(path));
        Git git = Git.cloneRepository()
                .setURI(url)
                .setDirectory(new File(path))
                .setBranchesToClone(Arrays.asList("refs/heads/" + branch))
                .setBranch(branch)
                .call();
      }

}
