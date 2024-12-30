package cn.xhh.tool.sdk.test;

import com.opencsv.CSVWriter;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GitDiffAnalyzer1 {
    private static final String REPO_NAME = "C:\\Users\\xie\\Desktop\\研究生\\catalog";
    private static final String CMT_CSV_FILE_PATH = "E:\\data\\scvtest\\catalogCommitAnalysisRes.csv";
    private static final String DIFF_CSV_FILE_PATH = "E:\\data\\scvtest\\catalogDiffAnalysisRes.csv";

    public static void main(String[] args) {
        try {
            // 打开 Git 仓库
            FileRepositoryBuilder builder = new FileRepositoryBuilder();
            Repository repository = builder.setGitDir(new File(REPO_NAME + "/.git"))
                    .readEnvironment()
                    .findGitDir()
                    .build();

            // 创建 Git 对象
            Git git = new Git(repository);

            // 获取 Git 提交历史
            Iterable<RevCommit> commits = git.log().call();

            // 获取提交历史并存储到列表
            List<RevCommit> commitList = new ArrayList<>();
            for (RevCommit commit : commits) {
                commitList.add(commit);
            }

            // 初始化 CSV 写入器
            String[] headerCommit = {"projectName", "commit", "parentCommit","author", "message", "date", "addLineNum", "deleteLineNum"};
            CSVWriter writerCmt = new CSVWriter(new FileWriter(CMT_CSV_FILE_PATH));
            writerCmt.writeNext(headerCommit);

            String[] headerDiff = {"commit", "changedFile",  "changeType", "diffAddedLineNum", "diffDeleteLineNum", "diffContent"};
            CSVWriter writerDiff = new CSVWriter(new FileWriter(DIFF_CSV_FILE_PATH));
            writerDiff.writeNext(headerDiff);

            // 使用 DiffFormatter 格式化差异
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            DiffFormatter diffFormatter = new DiffFormatter(outputStream);
            diffFormatter.setRepository(repository);
            diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
            diffFormatter.setDetectRenames(true);

            // 遍历每个提交
            for (int i = 0; i < commitList.size(); i++) {
                RevCommit commit = commitList.get(i);
                RevCommit parentCommit = (i + 1 < commitList.size()) ? commitList.get(i + 1) : null;

                System.out.println("Commit: " + commit.getId().getName());
                System.out.println("Author: " + commit.getAuthorIdent().getName());
                System.out.println("Message: " + commit.getFullMessage());
                System.out.println("Date: " + commit.getAuthorIdent().getWhen());

                String[] rowCommit = new String[8];
                rowCommit[0] = "code_review";
                rowCommit[1] = commit.getId().getName();
                rowCommit[2] = (parentCommit != null) ? parentCommit.getId().getName() : "";
                rowCommit[3] = commit.getAuthorIdent().getName();
                rowCommit[4] = commit.getFullMessage();
                rowCommit[5] = String.valueOf(commit.getAuthorIdent().getWhen());

                int cmtAddedLines = 0;
                int cmtDeletedLines = 0;

                if (parentCommit != null) {
                    // 比较父提交和当前提交
                    CanonicalTreeParser oldTreeParser = new CanonicalTreeParser();
                    CanonicalTreeParser newTreeParser = new CanonicalTreeParser();
                    ObjectReader reader = repository.newObjectReader();

                    oldTreeParser.reset(reader, parentCommit.getTree());
                    newTreeParser.reset(reader, commit.getTree());

                    List<DiffEntry> diffEntries = diffFormatter.scan(oldTreeParser, newTreeParser);
                    for (DiffEntry diff : diffEntries) {
                        if (diff.getNewPath().startsWith(".idea") || diff.getNewPath().startsWith(".gitignore")) {
                            continue;  // 跳过指定文件
                        }
                        FileHeader fileHeader = diffFormatter.toFileHeader(diff);
                        EditList editList = fileHeader.toEditList();

                        int diffAddedLines = 0;
                        int diffDeletedLines = 0;

                        for (Edit edit : editList) {
                            diffAddedLines += edit.getEndB() - edit.getBeginB();
                            diffDeletedLines += edit.getEndA() - edit.getBeginA();
                            cmtAddedLines += edit.getEndB() - edit.getBeginB();
                            cmtDeletedLines += edit.getEndA() - edit.getBeginA();
                        }

                        String[] rowDiff = new String[6];
                        rowDiff[0] = commit.getId().getName();
                        rowDiff[1] = diff.getNewPath();
                        rowDiff[2] = String.valueOf(diff.getChangeType());
                        rowDiff[3] = String.valueOf(diffAddedLines);
                        rowDiff[4] = String.valueOf(diffDeletedLines);
                        // 使用 ByteArrayOutputStream 来捕获格式化后的差异输出
                        diffFormatter.format(diff);
                        System.out.println(outputStream.toString());
                        rowDiff[5] = outputStream.toString();
                        writerDiff.writeNext(rowDiff);
                        outputStream.reset();  // 清空输出流，以便下一个差异使用
                    }



                } else {
                    // 处理初始提交（没有父提交）
                    System.out.println("This is the initial commit, no parent to compare.");
                }

                System.out.println("本次提交新增行数：" + cmtAddedLines);
                System.out.println("本次提交删除行数：" + cmtDeletedLines);

                rowCommit[6] = String.valueOf(cmtAddedLines);
                rowCommit[7] = String.valueOf(cmtDeletedLines);
                writerCmt.writeNext(rowCommit);
            }

            writerCmt.close(); // 关闭 CSV 写入器
            writerDiff.close();
            diffFormatter.close(); // 关闭 DiffFormatter
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
        }
    }
}
