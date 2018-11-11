package com.github.kieuthang.login_chat.data.common

import android.content.Context
import android.text.TextUtils
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.data.common.exception.MissingCredentialsException
import okhttp3.ResponseBody
import java.io.IOException

open class BaseRepositoryImpl(protected var mContext: Context) {
    companion object {

        fun hasError(result: Any?, code: Int, errorBody: ResponseBody?): Throwable? {
            try {
                if (errorBody != null) {
                    val errorMessage = errorBody.string()
                    AppLog.d(AppConstants.TAG, "hasError:$code,errorBody:$errorMessage")
                    if (code == AppConstants.APICodeResponse.USER_DOES_NOT_EXISTED || code == AppConstants.APICodeResponse.CODE_ERROR_401 || code == AppConstants.APICodeResponse.CODE_ERROR_500 || !TextUtils.isEmpty(errorMessage)) {
                        return MissingCredentialsException()
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return if (result == null || errorBody != null) {
                Throwable("Error")
            } else null
        }
    }
}
