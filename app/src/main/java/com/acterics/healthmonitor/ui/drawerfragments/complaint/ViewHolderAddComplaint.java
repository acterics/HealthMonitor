package com.acterics.healthmonitor.ui.drawerfragments.complaint;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.acterics.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oleg on 21.05.17.
 */

public class ViewHolderAddComplaint {

    @BindView(R.id.et_content) EditText etContent;

    private AddComplaintCallbacks callbacks;

    public ViewHolderAddComplaint(View view, AddComplaintCallbacks callbacks) {
        ButterKnife.bind(this, view);
        this.callbacks = callbacks;
    }

    @OnClick(R.id.bt_add)
    void addComplaint() {
        String content = etContent.getText().toString();
        if (content.isEmpty()) {
            Toast.makeText(etContent.getContext(), "Empty complaint", Toast.LENGTH_SHORT).show();
            etContent.requestFocus();
        } else {
            callbacks.onAddComplaint(content);
        }
    }


    @FunctionalInterface
    public interface AddComplaintCallbacks {
        void onAddComplaint(String content);
    }
}
