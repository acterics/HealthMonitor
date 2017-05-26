package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.content.Context
import android.content.res.TypedArray
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
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
    private val mSettingIcons: TypedArray
    private val mContext: Context = context
    private val mFragmentMap: Map<Int, Fragment>

    private var mOnNewFragmentListener: OnNewFragmentListener? = null


    override fun getItemCount() = mSettingTitles.size

    override fun onBindViewHolder(holder: SettingsHolder?, position: Int) {
        holder?.tvTitle?.text = mSettingTitles[position]
        holder?.tvTitle?.setCompoundDrawablesRelativeWithIntrinsicBounds(mSettingIcons.getResourceId(position, -1), 0, 0, 0)
        holder?.itemView?.setOnClickListener { mOnNewFragmentListener?.onNewFragment(mFragmentMap[position]) }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SettingsHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.item_settgin, parent, false)
        return SettingsHolder(view)
    }

    public fun setOnNewFragmentListener(onNewFragmentListener: OnNewFragmentListener) {
        this.mOnNewFragmentListener = onNewFragmentListener
    }


    class SettingsHolder(view: View): RecyclerView.ViewHolder(view) {
        val tvTitle: TextView by lazy { view.findViewById(R.id.tv_setting_title) as TextView }

    }


    companion object {
        val PERSONAL_INFO = 0
        val DEVICES = 1
        val HELP = 2
    }
    init {
        mSettingTitles = mContext.resources.getStringArray(R.array.settings_titles)
        mSettingIcons = mContext.resources.obtainTypedArray(R.array.settings_icons)
        mFragmentMap = LinkedHashMap<Int, Fragment>()
        mFragmentMap[PERSONAL_INFO] = PersonalInfoFragment()
        mFragmentMap[DEVICES] = Fragment()
        mFragmentMap[HELP] = Fragment()
    }

    public interface OnNewFragmentListener {
        fun onNewFragment(fragment: Fragment?)
    }
}

