package com.github.kieuthang.login_chat.views.common


import android.view.View

interface IEventListener {
    fun onClickBack()

    fun showKeyboard(view: View)

    fun hideKeyboard(view: View)

    fun showNoNetworkError()

    fun onShowError(message: String)

    fun tokenNotValid()

    fun tokenRenew()
}
