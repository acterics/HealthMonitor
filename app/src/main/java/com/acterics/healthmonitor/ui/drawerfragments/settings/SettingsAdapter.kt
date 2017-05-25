package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.acterics.healthmonitor.R

/**
 * Created by root on 26.05.17.
 */
class SettingsAdapter(context: Context) : RecyclerView.Adapter<SettingsAdapter.SettingsHolder>() {

    private val mSettingTitles: Array<String>
    private val mContext: Context = context


    override fun getItemCount() = mSettingTitles.size

    override fun onBindViewHolder(holder: SettingsHolder?, position: Int) {
        holder?.tvTitle?.text = mSettingTitles[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_settgin, parent, false)
        return SettingsHolder(view)
    }


    class SettingsHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView by lazy { view.findViewById(R.id.tv_setting_title) as TextView }
    }

    init {
        mSettingTitles = mContext.resources.getStringArray(R.array.settings_titles)
    }
}

