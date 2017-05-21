package com.acterics.healthmonitor.services.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


/**
 * Created by oleg on 15.02.17.
 */

public class HMFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("DEBUG_LOG", "onTokenRefresh: token" + token);
        
    }
}
