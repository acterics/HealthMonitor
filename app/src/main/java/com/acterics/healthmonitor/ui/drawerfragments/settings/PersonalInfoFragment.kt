package com.acterics.healthmonitor.ui.drawerfragments.settings

import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.text.Selection
import android.view.*
import android.widget.EditText
import android.widget.Toast
import com.acterics.healthmonitor.R
import com.acterics.healthmonitor.base.BaseCallback
import com.acterics.healthmonitor.data.RestClient
import com.acterics.healthmonitor.data.models.categories.user.UserModel
import com.acterics.healthmonitor.receivers.ErrorBroadcastReceiver
import com.acterics.healthmonitor.receivers.ErrorCode
import com.acterics.healthmonitor.utils.KeyboardUtils
import com.acterics.healthmonitor.utils.PreferenceUtils
import dmax.dialog.SpotsDialog


/**
 * Created by root on 26.05.17.
 */

class PersonalInfoFragment: Fragment() {

    private val etFirstName: EditText by lazy {view?.findViewById(R.id.et_first_name) as EditText}
    private val holderFirstName: TextInputLayout by lazy { view?.findViewById(R.id.holder_first_name) as TextInputLayout }
    private val etLastName: EditText by lazy {view?.findViewById(R.id.et_last_name) as EditText}
    private val holderLastName: TextInputLayout by lazy {view?.findViewById(R.id.holder_last_name) as TextInputLayout}

    private val mProgressDialog: SpotsDialog by lazy {SpotsDialog(context, R.style.loading_dialog)}
    private val mUserInfo: UserModel by lazy {PreferenceUtils.getUserModel(context)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean = when(item?.itemId) {
        R.id.action_submit -> submitChanges()
        else -> false
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_personal_info, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFirstName.setText(mUserInfo.firstName)
        etLastName.setText(mUserInfo.lastName)

    }

    private fun submitChanges() : Boolean {
        mProgressDialog.show()
        mUserInfo.firstName = etFirstName.text.toString()
        mUserInfo.lastName = etLastName.text.toString()

        RestClient.getApiService().changeUser(PreferenceUtils.getRequestUserToken(context), mUserInfo)
                .enqueue(object: BaseCallback<UserModel>(context,
                        OnRequestErrorListener { _, _ -> mProgressDialog.dismiss() }, true) {
                    override fun onSuccess(body: UserModel) {
                        PreferenceUtils.saveUserInfo(context, body)
                        KeyboardUtils.hide(activity)
                        mProgressDialog.dismiss()
                    }
                })
        return true
    }


}

