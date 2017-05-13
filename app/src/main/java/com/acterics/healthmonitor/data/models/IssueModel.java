package com.acterics.healthmonitor.data.models;

import com.acterics.healthmonitor.ui.issues.IssueCategory;

/**
 * Created by oleg on 13.05.17.
 */

public class IssueModel {
    private IssueCategory category;
    private String issueTitle;
    private String issueSummary;

    public IssueCategory getCategory() {
        return category;
    }

    public void setCategory(IssueCategory category) {
        this.category = category;
    }

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getIssueSummary() {
        return issueSummary;
    }

    public void setIssueSummary(String issueSummary) {
        this.issueSummary = issueSummary;
    }
}
