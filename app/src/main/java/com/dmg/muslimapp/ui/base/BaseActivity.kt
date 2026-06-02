package com.dmg.muslimapp.ui.base

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import butterknife.Unbinder
import com.dmg.muslimapp.BaseApplication
import com.dmg.muslimapp.utils.CommonUtils
import com.dmg.muslimapp.utils.NetworkUtils
import com.franmontiel.localechanger.LocaleChanger
import com.franmontiel.localechanger.utils.ActivityRecreationHelper
import com.google.android.gms.analytics.Tracker
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper

open class BaseActivity : AppCompatActivity(), BaseFragment.Callback{

    private lateinit var mUnBinder: Unbinder

    //lateinit var mProgressDialog : ProgressDialog
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun attachBaseContext(newBase: Context) {
        var newBase = newBase
        newBase = LocaleChanger.configureBaseContext(newBase)

        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onResume() {
        super.onResume()
        ActivityRecreationHelper.onResume(this)
    }

    override fun onDestroy() {
        ActivityRecreationHelper.onDestroy(this)
        super.onDestroy()
    }

    override fun onFragmentAttached() {

    }

    override fun onFragmentDetached(tag: String) {

    }

    fun showMessage(message: String?) {
        if(message!=null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    fun showMessage(@StringRes resId: Int) {
        showMessage(getString(resId))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
    }

    fun setToolbar(mToolbar: Toolbar) {
        setSupportActionBar(mToolbar)
    }

    fun setUnBinder(unBinder: Unbinder) {
        mUnBinder = unBinder
    }

    fun showBackButton(bool: Boolean) {
        supportActionBar!!.setDisplayHomeAsUpEnabled(bool)
        supportActionBar!!.setDisplayShowHomeEnabled(bool)
    }

    fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(applicationContext)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    fun showLoading() {
        hideLoading()
        mProgressDialog = CommonUtils.showLoadingDialog(this)
    }

    fun hideLoading() {
        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog!!.cancel()
        }
    }

    fun getGoogleTracker(): Tracker {
        return (application as BaseApplication).getDefaultTracker()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //PermissionManager.handleResult(this, requestCode, permissions, grantResults)
    }
}
