package cn.xhh.tool.sdk.infrastructure.git.dto;

/**
 * @Author rosemaryxxxxx
 * @Date 2024/12/30 16:29
 * @PackageName:cn.xhh.tool.sdk.infrastructure.git.dto
 * @ClassName: GitAnalysisResultDTO
 * @Description: TODO
 * @Version 1.0
 */
public class CommitAnalysisResultDTO {
    private String commit;
    private String projectName;
    private String parentCommit;
    private String author;
    private String message;
    private String date;
    private int addLineNum;
    private int deleteLineNum;

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getParentCommit() {
        return parentCommit;
    }

    public void setParentCommit(String parentCommit) {
        this.parentCommit = parentCommit;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getAddLineNum() {
        return addLineNum;
    }

    public void setAddLineNum(int addLineNum) {
        this.addLineNum = addLineNum;
    }

    public int getDeleteLineNum() {
        return deleteLineNum;
    }

    public void setDeleteLineNum(int deleteLineNum) {
        this.deleteLineNum = deleteLineNum;
    }
}
