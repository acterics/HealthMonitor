package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acterics.healthmonitor.R


/**
 * Created by root on 26.05.17.
 */

class PersonalInfoFragment: Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_personal_info, container, false)
    }

}