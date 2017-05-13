package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acterics.healthmonitor.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by oleg on 13.05.17.
 */

public class SignInFragment extends Fragment {

    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.holder_email) TextInputLayout holderEmail;
    @BindView(R.id.holder_password) TextInputLayout holderPassword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holderEmail.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holderPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    @OnClick(R.id.bt_sign_in)
    void onSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (validateInputs(email, password)) {

        }

    }


    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            holderEmail.setErrorEnabled(true);
            holderEmail.setError("Empty email!");
            etEmail.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            holderPassword.setErrorEnabled(true)
            ;
            holderPassword.setError("Empty password!");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}
