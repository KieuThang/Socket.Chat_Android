package com.github.kieuthang.login_chat.views.login_register


import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.PermissionChecker
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.KeyEvent
import android.view.View
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.common.utils.*
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.views.common.*
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : BaseFragmentActivity(), IDataLoadView {

    private var mLoginPresenter: DataPresenter? = null
    private val REQUEST_CODE_PERMISSION = 15123

    companion object {
        fun createLoginNewTaskIntent(context: Context): Intent {
            val intent = Intent(context, ActivityLogin::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mLoginPresenter = DataPresenter(this)
        mLoginPresenter!!.bindView(this)

        btnLogin.setOnClickListener { doLogin() }
        edtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable?) {
                if (editable != null && !TextUtils.isEmpty(editable.toString())) {
                    scrollView.scrollTo(0, btnLogin.bottom)
                }
            }
        })
        edtPassword.setOnKeyListener(View.OnKeyListener { view, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doLogin()
                return@OnKeyListener true
            }
            false
        })

        tvForgotPw.setOnClickListener { startActivity(ActivitySignUp.createIntent(this)) }
        ViewPressEffectHelper.attach(btnLogin)
        ViewPressEffectHelper.attach(tvForgotPw)


        val text = getString(R.string.plz_contact_us_to_discuss_the_matter)
        val ss = SpannableString(text)
        val span1 = object : ClickableSpan() {
            override fun onClick(textView: View) {
                ApplicationUtils.contactUsByMail(this@ActivityLogin)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.color = resources.getColor(android.R.color.holo_blue_light)
                ds.isUnderlineText = true
            }
        }

        ss.setSpan(span1, 21, 42, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvContactSupporter.text = ss
        tvContactSupporter.movementMethod = LinkMovementMethod.getInstance()

        requestNeededPermissions()

    }

    private fun requestNeededPermissions(): Boolean {
        val storagePermission = PermissionChecker.checkSelfPermission(this@ActivityLogin, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val permissions = ArrayList<String>()

        if (storagePermission != PermissionChecker.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (permissions.size > 0)
            PermissionUtils.requestMultiPermissions(this@ActivityLogin, permissions.toTypedArray(), REQUEST_CODE_PERMISSION)
        return (permissions.size > 0)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_PERMISSION -> {
                val allGranted = grantResults.none { it != PermissionChecker.PERMISSION_GRANTED }
                if (!allGranted) {
                    showDialog(PopupDialogFragment.Type.WARNING, 0, getString(R.string.plz_accept_the_needed_permission_first), "", false, getString(R.string.ok), "", 0, object : IPopupDialogFragment {
                        override fun clickPositiveText(requestCode: Int) {
                            finish()
                        }

                        override fun clickNegativeText(requestCode: Int) {

                        }
                    })
                }
            }
        }
    }

    private fun doLogin() {
        layoutBlockDeleted.visibility = View.GONE
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        if(TextUtils.isEmpty(email)){
            showError(getString(R.string.email_address_cannot_be_empty))
            return
        }
        if (!StringUtils.verifyEmail(email)) {
            showError(getString(R.string.email_address_is_not_valid))
            return
        }
        if (TextUtils.isEmpty(password)) {
            showError(getString(R.string.password_cannot_be_empty))
            return
        }
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoNetworkError()
            return
        }
        super.onHideKeyBoard(edtEmail)
        mLoginPresenter!!.login(email, password)
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
        if (accessToken == null) {
            showToastMessage(getString(R.string.incorrect_email_or_password))
            return
        }

        if (!accessToken.isActive) {
            layoutBlockDeleted.visibility = View.VISIBLE
            tvBlockedDeleted.text = getString(R.string.your_account_is_temporarily_blocked)
            Handler().postDelayed({ scrollView.scrollTo(0, scrollView.bottom) }, 500)
            return
        }
        if (accessToken.isDeleted) {
            layoutBlockDeleted.visibility = View.VISIBLE
            tvBlockedDeleted.text = getString(R.string.your_account_is_temporarily_deleted)
            Handler().postDelayed({ scrollView.scrollTo(0, scrollView.bottom) }, 500)
            return
        }

        onHideKeyBoard(edtEmail)
        mLoginPresenter!!.getMyProfile(true)

    }
}
