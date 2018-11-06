package com.github.kieuthang.login_chat.views.common


import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.common.utils.NetworkUtils
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.UserResponseModel

abstract class BaseFragmentActivity : AppCompatActivity(), IDataLoadView {
    protected var mDataPresenter: DataPresenter? = null
    private var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataPresenter = DataPresenter(this)
        mDataPresenter!!.bindView(this)
    }

    override fun onResume() {
        super.onResume()
        Handler().postDelayed({
            if (!NetworkUtils.isNetworkAvailable(this@BaseFragmentActivity)) {
                showNoNetworkError()
            }
        }, 1000)

    }

    fun showToastMessage(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    protected fun showNoNetworkError() {
        showToastMessage(getString(R.string.network_is_not_available))
    }

    protected fun showLongToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    protected fun onShowLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
            mProgressDialog!!.setTitle("")
            mProgressDialog!!.setMessage(getString(R.string.loading))
        }
        try {
            if (mProgressDialog!!.isShowing)
                return
            mProgressDialog!!.show()
        } catch (ignored: Exception) {
            ignored.printStackTrace()
        }
    }

    protected fun onHideLoading() {
        if (mProgressDialog == null)
            return
        try {
            mProgressDialog!!.dismiss()
        } catch (ignored: Exception) {

        }
    }

    fun onShowKeyBoard(view: View) {
        AppLog.d(AppConstants.TAG, "showKeyBoard")
        view.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun onHideKeyBoard(view: View) {
        AppLog.d(AppConstants.TAG, "onHideKeyBoard")
        var currentFocus = currentFocus
        if (currentFocus == null)
            currentFocus = view
        currentFocus.clearFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }

    protected fun showDialog(type: PopupDialogFragment.Type, content: String, subContent: String, isCancelable: Boolean, listener: IPopupDialogFragment?) {
        if (TextUtils.isEmpty(content))
            return
        showDialog(type, 0, content, subContent, isCancelable, "", "", 0, listener)
    }

    protected fun showDialog(type: PopupDialogFragment.Type, content: String, subContent: String, isCancelable: Boolean, icon: Int, listener: IPopupDialogFragment?) {
        if (TextUtils.isEmpty(content))
            return
        showDialog(type, 0, content, subContent, isCancelable, "", "", icon, listener)
    }


    protected fun showDialog(type: PopupDialogFragment.Type, requestCode: Int, content: String?, subContent: String?, isCancelable: Boolean, positiveText: String?, negativeText: String?, icon: Int, listener: IPopupDialogFragment?) {
        if (TextUtils.isEmpty(content))
            return
        val popUpDialogFragment = PopupDialogFragment()
        popUpDialogFragment.setListener(listener)
        val builder = PopupDialogFragment
        builder.mContent = content
        builder.type = type
        builder.requestCode = requestCode
        builder.mSubContent = subContent
        builder.isCancelable = isCancelable
        builder.positiveText = positiveText
        builder.negativeText = negativeText
        builder.mIcon = icon
        popUpDialogFragment.setBuilder(builder)
        try {
            popUpDialogFragment.show(fragmentManager, PopupDialogFragment::class.java.simpleName)
        } catch (e: Exception) {
            AppLog.e(AppConstants.TAG, "Show Dialog Exception:" + e.message)
        }
    }


    override fun onLoginResult(accessToken: AccessToken?) {

    }

    override fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?) {

    }

    override fun showLoading() {
        onShowLoading()
    }

    override fun hideLoading() {
        onHideLoading()
    }

    override fun showError(errorMessage: String) {
        showToastMessage(errorMessage)
    }
}
