package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseAuthCallback;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.utils.NavigationUtils;

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
        View view = inflater.inflate(R.layout.fragment_welcome_back, container, false);
        ButterKnife.bind(this, view);

        setEditTextErrorTextWatcher(etEmail, holderEmail);
        setEditTextErrorTextWatcher(etPassword, holderPassword);

        return view;
    }

    @OnClick(R.id.bt_sign_in)
    void onSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (validateInputs(email, password)) {
            SignInRequest request = new SignInRequest();
            request.setEmail(email);
            request.setPassword(password);
            RestClient.getApiService().signIn(request)
                    .enqueue(new BaseAuthCallback(getContext(),
                            body -> {
                                NavigationUtils.toMain(getContext());
                                getActivity().finish();
                            }));
        }



    }

    @OnClick(R.id.bt_sign_up)
    void onSignUp() {
        getFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addToBackStack("Stack")
                .replace(R.id.holder_content, new SignUpFragment())
                .commit();
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

    private static void setEditTextErrorTextWatcher(EditText et, TextInputLayout holder) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                holder.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
