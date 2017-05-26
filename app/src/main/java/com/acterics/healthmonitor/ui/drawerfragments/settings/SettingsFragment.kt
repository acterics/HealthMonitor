package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
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
    private val onNewFragmentListener: SettingsAdapter.OnNewFragmentListener


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater?.inflate(R.layout.fragment_settings, container, false)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mSettingsAdapter.setOnNewFragmentListener(onNewFragmentListener)
        rvSettingsList.layoutManager = LinearLayoutManager(context)
        rvSettingsList.adapter = mSettingsAdapter

    }



    companion object {
        val SETTINGS_ENTRY_POINT = "com.acterics.healthmonitor.ui.drawerfragments.settings.SETTINGS_ENTRY_POINT"
    }

    init {
        onNewFragmentListener = object : SettingsAdapter.OnNewFragmentListener {
            override fun onNewFragment(fragment: Fragment?) {
                fragmentManager
                        .beginTransaction()
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                        .addToBackStack(SETTINGS_ENTRY_POINT)
                        .replace(R.id.holder_content, fragment)
                        .commit()
            }

        }
    }

    inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T


}

