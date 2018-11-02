package com.github.kieuthang.login_chat.common.utils


import android.content.Context
import android.net.ConnectivityManager
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.common.AppConstants


class NetworkUtils {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val isNetworkAvailable = connectivityManager.activeNetworkInfo != null && connectivityManager.activeNetworkInfo.isConnected
            AppLog.d(AppConstants.TAG, "isNetworkAvailable = $isNetworkAvailable")
            return isNetworkAvailable
        }
    }
}
