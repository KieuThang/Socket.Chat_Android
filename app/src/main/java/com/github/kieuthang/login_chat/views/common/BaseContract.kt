package com.github.kieuthang.login_chat.views.common

interface BaseContract {
    interface View {
        fun showLoading()
        fun hideLoading()
        fun showError(errorMessage: String)
    }

    interface Presenter<in T : BaseContract.View> {
        fun bindView(view: T)
        fun release()
    }
}
