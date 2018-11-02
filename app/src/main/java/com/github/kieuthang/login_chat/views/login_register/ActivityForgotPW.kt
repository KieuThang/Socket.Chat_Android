package com.github.kieuthang.login_chat.views.login


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
import com.github.kieuthang.login_chat.data.user.entity.AccessToken
import com.github.kieuthang.login_chat.data.user.entity.BaseResponseModel
import com.github.kieuthang.login_chat.data.user.entity.UserResponseModel
import com.github.kieuthang.login_chat.views.common.BaseFragmentActivity
import kotlinx.android.synthetic.main.activity_forgot_pw.*

class ActivityForgotPW : BaseFragmentActivity(), ILoginDataLoadView {
    override fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?) {

    }

    private var mPresenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pw)
        mPresenter = LoginPresenter(this)
        mPresenter!!.bindView(this)
        btnForgotPw.setOnClickListener { doForgotPW() }
        ViewPressEffectHelper.attach(btnForgotPw)

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
                    scrollView.scrollTo(0, btnForgotPw.bottom)
                }
            }
        })
    }

    private fun doForgotPW() {
        val email = edtEmail.text.toString()
        if (!StringUtils.verifyEmail(email)) {
            showError(getString(R.string.email_address_is_not_valid))
            return
        }
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoNetworkError()
            return
        }
        mPresenter!!.forgotPW(email)
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

    override fun onForgotPWResult(response: BaseResponseModel?) {
        if (response?.code == null) {
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }
        if(AppConstants.APICodeResponse.USER_DOES_NOT_EXISTED == response.code){
            showError(getString(R.string.user_does_not_exist))
            return
        }
        if(AppConstants.APICodeResponse.SUCCESS == response.code) {
            val email = edtEmail.text.toString()
            showToastMessage(getString(R.string.plz_check_your_email_to_reset_your_password, email))
        }else{
            showError(getString(R.string.something_went_wrong_please_try_again_later))
            return
        }
    }

    companion object {

        fun createIntent(context: Context): Intent {
            return Intent(context, ActivityForgotPW::class.java)
        }
    }
}
