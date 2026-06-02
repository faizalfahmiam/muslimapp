package com.dmg.muslimapp.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.widget.Toolbar
import android.util.Log
import butterknife.ButterKnife
import butterknife.OnClick
import com.dmg.muslimapp.R
import com.dmg.muslimapp.data.prefs.AppPreference
import com.dmg.muslimapp.ui.base.BaseActivity
import com.dmg.muslimapp.utils.LoggedStatus
import kotlinx.android.synthetic.main.user_login_activity.*

class LoginActivity: BaseActivity(), LoginView{
    private lateinit var mPresenter: LoginPresenter

    companion object {
        fun getStartIntent(context: Context): Intent {
            return Intent(context, LoginActivity::class.java)
        }
    }

    @Nullable
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_login_activity)
        ButterKnife.bind(this)
        setup()
    }

    fun setup(){
        setToolbar(toolbar as Toolbar)
        showBackButton(true)

        mPresenter = LoginPresenter(this)

        val login = LoggedStatus.LoggedInMode.LOGGED_IN_MODE_SERVER.type
        Log.e("logged", login.toString())
    }

    override fun setProfileSuccess(){
        if(AppPreference.getCurrentUserLoggedInMode() == LoggedStatus.LoggedInMode.LOGGED_IN_MODE_SERVER.type){
            finish()
        }else{
            showMessage("Login Failed")
        }
    }

    override fun setProfileFailed(message: String){
        showMessage(message)
    }

    override fun loginFailed(message: String){
        showMessage(message)
    }

    @OnClick(R.id.btn_login)
    fun doLogin(){
        val username = ed_username.text.toString()
        val pass = ed_password.text.toString()
        mPresenter.getLogin(this, username, pass)
    }

    @OnClick(R.id.btn_register)
    fun doRegister(){
        //
    }
}