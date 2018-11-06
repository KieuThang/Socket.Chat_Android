package com.github.kieuthang.login_chat.data


import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Default subscriber base class to be used whenever you want default error handling.
 */
open class DefaultSubscriber<T> : Observer<T> {
    override fun onError(e: Throwable) {
        AppLog.e(AppConstants.TAG, "onError")
    }

    override fun onComplete() {
    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onNext(t: T) {
        // no-op by default.
    }
}
