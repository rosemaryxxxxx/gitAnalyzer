package cn.xhh.tool.sdk.infrastructure.git.dto;

/**
 * @Author rosemaryxxxxx
 * @Date 2024/12/30 16:34
 * @PackageName:cn.xhh.tool.sdk.infrastructure.git.dto
 * @ClassName: DiffEntryDTO
 * @Description: TODO
 * @Version 1.0
 */
public class DiffEntryDTO {
    private String changedFile;
    private String commit;
    private String changeType;
    private int diffAddedLineNum;
    private int diffDeleteLineNum;
    private String diffContent;

    public String getChangedFile() {
        return changedFile;
    }

    public void setChangedFile(String changedFile) {
        this.changedFile = changedFile;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public int getDiffAddedLineNum() {
        return diffAddedLineNum;
    }

    public void setDiffAddedLineNum(int diffAddedLineNum) {
        this.diffAddedLineNum = diffAddedLineNum;
    }

    public int getDiffDeleteLineNum() {
        return diffDeleteLineNum;
    }

    public void setDiffDeleteLineNum(int diffDeleteLineNum) {
        this.diffDeleteLineNum = diffDeleteLineNum;
    }

    public String getDiffContent() {
        return diffContent;
    }

    public void setDiffContent(String diffContent) {
        this.diffContent = diffContent;
    }
}
