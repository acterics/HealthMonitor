package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.acterics.healthmonitor.R

/**
 * Created by root on 25.05.17.
 */

class SettingsFragment : Fragment() {
    private val rvSettingsList: RecyclerView by lazy { find<RecyclerView>(R.id.rv_settings) }
    private val mSettingsAdapter: SettingsAdapter by lazy { SettingsAdapter(context) }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_settings, container, false)



        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvSettingsList.layoutManager = LinearLayoutManager(context)
        rvSettingsList.adapter = mSettingsAdapter
    }

    inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T


}

