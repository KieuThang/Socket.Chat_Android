package com.github.kieuthang.login_chat.data.common.cache

import android.content.Context
import android.content.pm.PackageManager
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.github.kieuthang.login_chat.data.common.SharePrefUtils


import java.io.File

public abstract class Cache(protected val mContext: Context) {

    fun isCached(cacheName: String): Boolean {
        try {
            val manager = mContext.packageManager ?: return false
            val fileDir = manager.getPackageInfo(mContext.packageName, 0)
                    .applicationInfo.dataDir + "/shared_prefs/" + cacheName + ".xml"
            val f = File(fileDir)
            return f.exists()
        } catch (e: PackageManager.NameNotFoundException) {
            AppLog.e(AppConstants.TAG, e.message)
            return false
        }
    }

    protected fun put(cacheName: String, jsonData: String, expirationTimeMillis: Long) {
        SharePrefUtils.putLongValue(mContext, cacheName, AppConstants.Prefs.KEY_EXPIRATION_TIME, expirationTimeMillis)
        SharePrefUtils.putStringValue(mContext, cacheName, AppConstants.Prefs.KEY_JSON_DATA, jsonData)
    }

    fun put(cacheName: String, jsonData: String) {
        SharePrefUtils.putStringValue(mContext, cacheName, AppConstants.Prefs.KEY_JSON_DATA, jsonData)
    }

    fun getJsonData(cacheName: String): String {
        return SharePrefUtils.getStringValue(mContext, cacheName, AppConstants.Prefs.KEY_JSON_DATA)
    }

    fun isCacheExpired(cacheName: String): Boolean {
        return System.currentTimeMillis() > SharePrefUtils.getLongValue(mContext, cacheName, AppConstants.Prefs.KEY_EXPIRATION_TIME)
    }

    fun clearCache(cacheName: String): Boolean {
        AppLog.d(AppConstants.TAG, "clearCache :" + cacheName)
        SharePrefUtils.removeSharedPref(mContext, cacheName)
        return false
    }
}
