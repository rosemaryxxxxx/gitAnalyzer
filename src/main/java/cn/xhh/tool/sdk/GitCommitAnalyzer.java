package cn.xhh.tool.sdk;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.attributes.Attribute;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.lib.Constants;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitCommitAnalyzer {

    public static void main(String[] args) {
        try {
            // 打开 Git 仓库
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File("D:\\study\\xfgstudy\\MiddlewareDesign\\openai-code-review\\.git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            // 创建 Git 对象
            Git git = new Git(repository);

            // 获取 Git 提交历史
            Iterable<RevCommit> commits = git.log().call();

            // 创建 DiffFormatter，用于比较两个提交之间的代码差异
            DiffFormatter diffFormatter = new DiffFormatter(System.out);
            diffFormatter.setRepository(repository);

            // 遍历每个提交并获取代码差异
            for (RevCommit commit : commits) {
                System.out.println("Commit: " + commit.getId().getName());
                System.out.println("Author: " + commit.getAuthorIdent().getName());
                System.out.println("Data: " + commit.getAuthorIdent().getWhen());
                System.out.println("Message: " + commit.getFullMessage());

                String s = commit.getTree().toString();

                // 获取提交前后代码的差异
                try (RevWalk revWalk = new RevWalk(repository)) {
                    revWalk.markStart(commit);
                    RevCommit parentCommit = revWalk.next();

                    if (true) {
                        CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
                        ObjectReader reader = repository.newObjectReader();
//                        oldTreeParser.reset(reader, parentCommit.getTree());

                        CanonicalTreeParser newTreeParser = new CanonicalTreeParser();
                        newTreeParser.reset(reader, commit.getTree());

                        // 输出差异
                        List<DiffEntry> diffEntries = diffFormatter.scan(oldTreeParser, newTreeParser);
                        for (DiffEntry diff : diffEntries) {
                            System.out.println("Changed file: " + diff.getNewPath());
                            Attribute diffAttribute = diff.getDiffAttribute();
                        }
                    }
                }

                System.out.println("------------------------------------------------");
            }
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
