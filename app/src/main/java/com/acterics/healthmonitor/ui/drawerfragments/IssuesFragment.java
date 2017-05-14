package com.acterics.healthmonitor.ui.drawerfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.mock.MockData;
import com.acterics.healthmonitor.ui.issues.IssuesListAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 13.05.17.
 */

public class IssuesFragment extends Fragment {

    @BindView(R.id.rv_issues) RecyclerView rvIssues;
    @BindView(R.id.fab_add_issue) FloatingActionButton fabAddIssue;

    private IssuesListAdapter issuesListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_issues, container, false);
        ButterKnife.bind(this, view);

        issuesListAdapter = new IssuesListAdapter();
        issuesListAdapter.setIssues(MockData.getIssues());

        rvIssues.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIssues.setAdapter(issuesListAdapter);

        return view;
    }
}
