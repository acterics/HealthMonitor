package com.acterics.healthmonitor.ui.auth;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.acterics.healthmonitor.R;
import com.acterics.healthmonitor.base.BaseCallback;
import com.acterics.healthmonitor.data.RestClient;
import com.acterics.healthmonitor.data.models.rest.requests.SignInRequest;
import com.acterics.healthmonitor.data.models.rest.responses.AuthResponse;
import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver;
import com.acterics.healthmonitor.utils.NavigationUtils;
import com.acterics.healthmonitor.utils.PreferenceUtils;
import com.acterics.healthmonitor.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import timber.log.Timber;

/**
 * Created by oleg on 13.05.17.
 */

public class SignInFragment extends Fragment {

    @BindView(R.id.et_email) EditText etEmail;
    @BindView(R.id.et_password) EditText etPassword;
    @BindView(R.id.holder_email) TextInputLayout holderEmail;
    @BindView(R.id.holder_password) TextInputLayout holderPassword;

    private SpotsDialog loadingDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);

        ViewUtils.setEditTextErrorTextWatcher(etEmail, holderEmail);
        ViewUtils.setEditTextErrorTextWatcher(etPassword, holderPassword);
        etEmail.setText(PreferenceUtils.getLastUserEmail(getContext()));

        return view;
    }

    @OnClick(R.id.bt_sign_in)
    void onSignIn() {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if (validateInputs(email, password)) {
            loadingDialog = new SpotsDialog(getContext(), R.style.loading_dialog);
            loadingDialog.show();
            SignInRequest request = new SignInRequest();
            request.setEmail(email);
            request.setPassword(password);
            RestClient.getApiService().signIn(request)
                    .enqueue(new BaseCallback<AuthResponse>(getContext(), (errorCode, serverError) -> {
                        loadingDialog.dismiss();
                        if (serverError == ErrorBroadcastReceiver.USER_NOT_FOUND) {
                            Timber.e("onSignIn: %d", serverError);
                            getFragmentManager().beginTransaction()
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                    .addToBackStack(null)
                                    .replace(R.id.holder_content, SignUpFragment.newInstance(email, password))
                                    .commit();
                        }
                    }, false)
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
