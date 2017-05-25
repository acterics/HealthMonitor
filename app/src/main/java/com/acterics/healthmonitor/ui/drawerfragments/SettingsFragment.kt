package com.acterics.healthmonitor.ui.drawerfragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.ButterKnife
import com.acterics.healthmonitor.R

/**
 * Created by root on 25.05.17.
 */

class SettingsFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_settings, container, false)



        return view
    }

}