package com.github.kieuthang.login_chat.data.common.cache


import android.content.Context
import android.text.TextUtils
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.UserResponseModel

import com.google.gson.Gson

class DataCacheApiImpl(context: Context) : Cache(context), IDataCacheApi {
    override fun getAccessToken(): AccessToken? {
        val userModelJson = getJsonData(AppConstants.Cache.ACCESS_TOKEN)
        if (TextUtils.isEmpty(userModelJson)) {
            return null
        }
        return Gson().fromJson(userModelJson, AccessToken::class.java)
    }

    override fun getUserModel(): UserResponseModel? {
        val userModelJson = getJsonData(AppConstants.Cache.USER_MODEL)
        if (TextUtils.isEmpty(userModelJson)) {
            return null
        }
        return Gson().fromJson(userModelJson, UserResponseModel::class.java)
    }

    override fun saveDataToCache(cacheName: String, jsonData: String) {
        clearCache(cacheName)
        put(cacheName, jsonData)
    }

    override fun doClearCache(cacheName: String) {
        clearCache(cacheName)
    }

    override fun isExpired(mapCacheName: String): Boolean {
        return isCacheExpired(mapCacheName)
    }
}
