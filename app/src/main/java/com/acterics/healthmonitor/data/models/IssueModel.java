package com.acterics.healthmonitor.data.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by oleg on 13.05.17.
 */

public class IssueModel {
    @SerializedName("category")
    private int category;

    @SerializedName("title")
    private String issueTitle;

    @SerializedName("content")
    private String issueSummary;

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
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
