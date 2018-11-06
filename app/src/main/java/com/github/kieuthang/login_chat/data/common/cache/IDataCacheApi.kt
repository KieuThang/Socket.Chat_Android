package com.github.kieuthang.login_chat.data.common.cache

import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.UserModel

interface IDataCacheApi {
    fun doClearCache(cacheName: String)

    fun saveDataToCache(cacheName: String, jsonData: String)

    fun isExpired(mapCacheName: String): Boolean

    fun getUserModel(): UserModel?

    fun getAccessToken(): AccessToken?
}
