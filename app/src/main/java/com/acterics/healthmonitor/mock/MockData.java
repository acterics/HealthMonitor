package com.acterics.healthmonitor.mock;

import com.acterics.healthmonitor.data.models.categories.complaint.IssueModel;
import com.acterics.healthmonitor.ui.drawerfragments.complaint.ComplaintCategory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by oleg on 14.05.17.
 * Debug only. Class that generate mock data.
 */

public class MockData {
    private static final String MOCK_TEXT = "Lorem ipsum. Lorem ipsum. Lorem ipsum. Lorem ipsum. Lorem ipsum. Lorem ipsum.";

    private MockData() {

    }

    public static List<IssueModel> getIssues() {
        List<IssueModel> issueModels = new ArrayList<>();
        IssueModel issue;
        for (int i = 0; i < 10; i++) {
            issue = new IssueModel();
            issue.setCategory(i % 3 == 0 ? ComplaintCategory.HEART.ordinal() : ComplaintCategory.STOMACH.ordinal());
            issue.setIssueTitle("Issue " + i);
            issue.setIssueSummary(MOCK_TEXT);
            issueModels.add(issue);
        }
        return issueModels;
    }
}
