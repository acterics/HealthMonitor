package com.acterics.healthmonitor.ui.drawerfragments.issues;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.data.models.IssueModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 13.05.17.
 */

public class IssuesListAdapter extends RecyclerView.Adapter<IssuesListAdapter.IssueHolder> {

    private List<IssueModel> issues;

    public IssuesListAdapter() {
        issues = new ArrayList<>();
    }

    public void setIssues(@NonNull List<IssueModel> issues) {
        this.issues.clear();
        this.issues.addAll(issues);
        notifyDataSetChanged();
    }

    public void addIssues(@NonNull List<IssueModel> issues) {
        this.issues.addAll(issues);
        notifyDataSetChanged();
    }

    public void addIssue(@NonNull IssueModel issue) {
        this.issues.add(issue);
        notifyDataSetChanged();
    }

    @Override
    public IssueHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_issue, parent, false);
        return new IssueHolder(view);
    }

    @Override
    public void onBindViewHolder(IssueHolder holder, int position) {
        IssueModel issue = issues.get(position);
        holder.tvIssueTitle.setText(issue.getIssueTitle());
        holder.tvIssueContent.setText(issue.getIssueSummary());
        holder.ivCategory.setImageResource(IssueCategory.getDrawable(issue.getCategory()));
    }


    @Override
    public int getItemCount() {
        return issues.size();
    }

    static class IssueHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_issue_title) TextView tvIssueTitle;
        @BindView(R.id.tv_issue_content) TextView tvIssueContent;
        @BindView(R.id.iv_category) ImageView ivCategory;

        public IssueHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
