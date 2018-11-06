package com.github.kieuthang.login_chat.views.login_register


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.utils.NetworkUtils
import com.github.kieuthang.login_chat.common.utils.StringUtils
import com.github.kieuthang.login_chat.common.utils.ViewPressEffectHelper
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.BaseResponseModel
import com.github.kieuthang.login_chat.views.ActivityHome
import com.github.kieuthang.login_chat.views.common.BaseFragmentActivity
import kotlinx.android.synthetic.main.activity_signup.*

class ActivitySignUp : BaseFragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        btnSignUp.setOnClickListener { doForgotPW() }
        ViewPressEffectHelper.attach(btnSignUp)

        edtEmail.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doForgotPW()
                return@OnKeyListener true
            }
            false
        })

        edtEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable != null && !TextUtils.isEmpty(editable.toString())) {
                    scrollView.scrollTo(0, btnSignUp.bottom)
                }
            }
        })
        tvGotoLogin.setOnClickListener {
            startActivity(ActivityLogin.createLoginNewTaskIntent(this))
            finish()
        }
    }

    private fun doForgotPW() {
        val email = edtEmail.text.toString()
        val firstName = edtFirstName.text.toString()
        val lastName = edtLastName.text.toString()
        val password = edtPassword.text.toString()

        if (TextUtils.isEmpty(firstName)) {
            showError(getString(R.string.first_name_cannot_be_empty))
            return
        }
        if (TextUtils.isEmpty(lastName)) {
            showError(getString(R.string.last_name_cannot_be_empty))
            return
        }
        if (TextUtils.isEmpty(password)) {
            showError(getString(R.string.password_cannot_be_empty))
            return
        }
        if (!StringUtils.verifyEmail(email)) {
            showError(getString(R.string.email_address_is_not_valid))
            return
        }
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoNetworkError()
            return
        }
        mDataPresenter!!.register(firstName, lastName, email, password)
    }

    override fun showLoading() {
        super.onShowLoading()
    }

    override fun hideLoading() {
        super.onHideLoading()
    }

    override fun showError(errorMessage: String) {
        showToastMessage(errorMessage)
    }

    override fun onLoginResult(accessToken: AccessToken?) {

    }

    override fun onRegisterResult(t: AccessToken?, throwable: Throwable?) {
        super.onRegisterResult(t, throwable)
        if(throwable != null){
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }
        var message = t!!.message!!.toString()
        if(t.code != AppConstants.APICodeResponse.SUCCESS){
            if(TextUtils.isEmpty(message)){
                message = getString(R.string.something_went_wrong_please_try_again_later)
            }
            showToastMessage(message)
            return
        }
        showToastMessage(message)
        startActivity(ActivityHome.createIntent(this))
        finish()
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ActivitySignUp::class.java)
        }
    }
}
