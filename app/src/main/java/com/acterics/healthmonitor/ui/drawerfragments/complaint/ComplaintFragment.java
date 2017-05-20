package com.acterics.healthmonitor.ui.drawerfragments.complaint;

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
import com.acterics.healthmonitor.data.models.Complaint;
import com.acterics.healthmonitor.utils.PreferenceUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oleg on 13.05.17.
 */

public class ComplaintFragment extends Fragment {

    @BindView(R.id.rv_issues) RecyclerView rvIssues;
    @BindView(R.id.fab_add_issue) FloatingActionButton fabAddIssue;

    private ComplaintListAdapter complaintListAdapter;
    private float destiny;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);
        ButterKnife.bind(this, view);

        destiny = getResources().getDisplayMetrics().density;
        complaintListAdapter = new ComplaintListAdapter();


        rvIssues.setLayoutManager(new LinearLayoutManager(getContext()));
        rvIssues.setAdapter(complaintListAdapter);
        requestIssues();

        return view;
    }

    private void requestIssues() {
        RestClient.getApiService().getComplaints(PreferenceUtils.getRequestUserToken(getContext()))
                .enqueue(new BaseCallback<List<Complaint>>(getContext()) {
                    @Override
                    public void onSuccess(@NonNull List<Complaint> body) {
                        complaintListAdapter.setComplaints(body);
                    }
                });
    }

//    boolean toggle = false;


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
