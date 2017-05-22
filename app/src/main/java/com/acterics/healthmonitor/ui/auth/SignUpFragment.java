package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.rest.requests.SignUpRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.acterics.healthmonitor.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;

/**
 * Created by oleg on 13.05.17.
 */

public class SignUpFragment extends Fragment {
    private static final String KEY_EMAIL = "com.acterics.healthmonitor.ui.auth.KEY_EMAIL";
    private static final String KEY = "com.acterics.healthmonitor.ui.auth.KEY";

    @BindView(R.id.et_first_name) EditText etFirstName;
    @BindView(R.id.et_last_name) EditText etLastName;
    @BindView(R.id.holder_first_name) TextInputLayout holderFirstName;
    @BindView(R.id.holder_last_name) TextInputLayout holderLastName;
    
    private String email;
    private String password;

    private SpotsDialog loadingDialog;

    public static SignUpFragment newInstance(String email, String password) {
        
        Bundle args = new Bundle();
        args.putString(KEY_EMAIL, email);
        args.putString(KEY, password);
        SignUpFragment fragment = new SignUpFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);
        email = getArguments().getString(KEY_EMAIL);
        password = getArguments().getString(KEY);

        ViewUtils.setEditTextErrorTextWatcher(etFirstName, holderFirstName);
        ViewUtils.setEditTextErrorTextWatcher(etLastName, holderLastName);

        return view;
    }

    @OnClick(R.id.bt_sign_up)
    void onSignUp() {
        String firstName = etFirstName.getText().toString();
        String lastName = etLastName.getText().toString();
        if (validateInputs(firstName, lastName)) {
            loadingDialog = new SpotsDialog(getContext(), R.style.loading_dialog);
            loadingDialog.show();
            SignUpRequest request = new SignUpRequest();
            request.setEmail(email);
            request.setPassword(password);
            request.setFirstName(firstName);
            request.setLastName(lastName);
            RestClient.getApiService().signUp(request)
                    .enqueue(new BaseCallback<AuthResponse>(getContext())
                    {
                        @Override
                        public void onSuccess(@NonNull AuthResponse body) {
                            loadingDialog.dismiss();
                            NavigationUtils.succesAuthorization(context, body);
                            getActivity().finish();
                        }
                    });
        }
    }

    private boolean validateInputs(String firstName, String lastName) {
        if (firstName.isEmpty()) {
            holderFirstName.setErrorEnabled(true);
            holderFirstName.setError("Empty first name!");
            etFirstName.requestFocus();
            return false;
        }
        if (lastName.isEmpty()) {
            holderLastName.setErrorEnabled(true);
            holderLastName.setError("Empty last name!");
            etLastName.requestFocus();
            return false;
        }
        return true;
    }
}
