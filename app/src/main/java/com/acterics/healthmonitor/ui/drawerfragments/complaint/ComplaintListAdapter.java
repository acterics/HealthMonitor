package com.acterics.healthmonitor.ui.drawerfragments.complaint;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.data.models.Complaint;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by oleg on 13.05.17.
 */

public class ComplaintListAdapter extends RecyclerView.Adapter<ComplaintListAdapter.ComplaintHolder> {

    private List<Complaint> complaints;

    public ComplaintListAdapter() {
        complaints = new ArrayList<>();
    }

    public void setComplaints(@NonNull List<Complaint> complaints) {
        this.complaints.clear();
        this.complaints.addAll(complaints);
        notifyDataSetChanged();
    }

    public void addComplaints(@NonNull List<Complaint> complaints) {
        this.complaints.addAll(complaints);
        notifyDataSetChanged();
    }

    public void addComplaint(@NonNull Complaint complaint) {
        this.complaints.add(complaint);
        notifyDataSetChanged();
    }

    @Override
    public ComplaintHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_complaint, parent, false);
        return new ComplaintHolder(view);
    }

    @Override
    public void onBindViewHolder(ComplaintHolder holder, int position) {
        Complaint complaint = complaints.get(position);
        holder.tvIssueContent.setText(complaint.getDescription());
//        holder.ivCategory.setImageResource(ComplaintCategory.getDrawable(issue.getCategory()));
    }


    @Override
    public int getItemCount() {
        return complaints.size();
    }

    static class ComplaintHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_complaint_content) TextView tvIssueContent;

        public ComplaintHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
