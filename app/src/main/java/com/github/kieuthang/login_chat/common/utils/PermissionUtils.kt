package com.github.kieuthang.login_chat.common.utils


import android.app.Activity
import android.support.v4.app.ActivityCompat

object PermissionUtils {
    fun requestMultiPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }
}
