package com.acterics.healthmonitor.ui.drawerfragments.issues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.IssueModel;
import com.acterics.healthmonitor.data.models.rest.requests.BaseUserInfoRequest;
import com.acterics.healthmonitor.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

        destiny = getResources().getDisplayMetrics().density;
        issuesListAdapter = new IssuesListAdapter();


        rvIssues.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIssues.setAdapter(issuesListAdapter);
        requestIssues();

        return view;
    }

    private void requestIssues() {
        BaseUserInfoRequest request = new BaseUserInfoRequest();
        PreferenceUtils.fillRequest(getContext(), request);
        RestClient.getApiService().getIssues(request)
                .enqueue(new BaseCallback<List<IssueModel>>(getContext()) {

                    @Override
                    public void onSuccess(@NonNull List<IssueModel> body) {
                        issuesListAdapter.setIssues(body);
                    }
                });
    }

    boolean toggle = false;
    float destiny;

    @OnClick(R.id.fab_add_issue) void onFabClick() {
//        if (toggle) {
//            fabAddIssue.animate()
//                    .scaleX(1)
//                    .translationX(0)
//                    .start();
//        } else {
//            fabAddIssue.animate()
////                    .scaleX(4)
//                    .translationX(-300)
//                    .start();
//        }
//        toggle = !toggle;

    }
}
