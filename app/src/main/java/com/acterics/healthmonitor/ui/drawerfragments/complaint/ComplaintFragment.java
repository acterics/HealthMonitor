package com.acterics.healthmonitor.ui.drawerfragments.complaint;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.base.BaseFragment;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.categories.complaint.Complaint;
import com.acterics.healthmonitor.data.models.rest.responses.ComplaintsResponse;
import com.acterics.healthmonitor.ui.views.CustomFAB;
import com.acterics.healthmonitor.utils.KeyboardUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.gordonwong.materialsheetfab.MaterialSheetFab;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import timber.log.Timber;

/**
 * Created by oleg on 13.05.17.
 */

public class ComplaintFragment extends BaseFragment {

    @BindView(R.id.rv_complaints) RecyclerView rvComplaints;
    @BindView(R.id.fab_add_issue) CustomFAB fabAddIssue;
    @BindView(R.id.overlay) View overlay;
    @BindView(R.id.fab_sheet) View cardView;


    private MaterialSheetFab<CustomFAB> fab;

    private ComplaintListAdapter complaintListAdapter;
    private SpotsDialog loadingDialog;
    private ViewHolderAddComplaint addComplaintHolder;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complaints, container, false);
        ButterKnife.bind(this, view);

        complaintListAdapter = new ComplaintListAdapter();
        int sheetColor = ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null);
        int fabColor = ResourcesCompat.getColor(getResources(), R.color.colorAccent, null);
        fab = new MaterialSheetFab<>(fabAddIssue, cardView, overlay,
                sheetColor, fabColor);

        addComplaintHolder = new ViewHolderAddComplaint(cardView, this::addComplaint);
        rvComplaints.setLayoutManager(new LinearLayoutManager(getContext()));
        rvComplaints.setAdapter(complaintListAdapter);
        loadingDialog = new SpotsDialog(getContext(), R.style.loading_dialog);

        requestComplaints();

        return view;
    }



    private void requestComplaints() {
        loadingDialog.show();
        RestClient.getApiService().getComplaints(PreferenceUtils.getRequestUserToken(getContext()))
                .enqueue(new BaseCallback<ComplaintsResponse>(getContext(),
                        (errorCode, serverError) -> loadingDialog.dismiss(), true) {
                    @Override
                    public void onSuccess(@NonNull ComplaintsResponse body) {
                        Timber.e("onSuccess: onSuccess");
                        complaintListAdapter.setComplaints(body.getComplaints());
                        loadingDialog.dismiss();
                    }
                });
    }

    private void addComplaint(String content) {
        Complaint complaint = new Complaint();
        complaint.setDescription(content);
        loadingDialog.show();
        RestClient.getApiService().addComplaint(PreferenceUtils.getRequestUserToken(getContext()), complaint)
                .enqueue(new BaseCallback<Complaint>(getContext(),
                        (errorCode, serverError) -> loadingDialog.dismiss(), true) {
                    @Override
                    public void onSuccess(@NonNull Complaint body) {
                        KeyboardUtils.hide(getActivity());
                        fab.hideSheet();
                        loadingDialog.dismiss();
                        complaintListAdapter.addComplaint(complaint);
                    }
                });
    }


    @Override
    public boolean onBackPressed() {
        if (fab.isSheetVisible()) {
            fab.hideSheet();
            return true;
        } else {
            return false;
        }
    }
}
