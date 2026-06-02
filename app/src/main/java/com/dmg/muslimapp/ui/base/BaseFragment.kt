package com.dmg.muslimapp.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.view.View

abstract class BaseFragment: Fragment() {
    private var mActivity: BaseActivity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUp(view)

        //locationDialog = CommonUtils.showLocationDialog(activity)
        //downloadDialog = CommonUtils.showDownloadDialog(activity, R.string.load_download_audio)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseActivity) {
            val activity = context as BaseActivity?
            this.mActivity = activity
            activity?.onFragmentAttached()
        }
    }

    protected abstract fun setUp(view: View)

    fun getBaseActivity() = mActivity

    fun showMessage(message: String) {
        if (mActivity != null) {
            mActivity!!.showMessage(message)
        }
    }

    fun showMessage(@StringRes resId: Int) {
        if (mActivity != null) {
            mActivity!!.showMessage(resId)
        }
    }

    override fun onDetach() {
        mActivity = null
        super.onDetach()
    }

    interface Callback {

        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }
}