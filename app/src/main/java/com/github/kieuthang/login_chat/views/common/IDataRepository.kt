package com.github.kieuthang.login_chat.views.common

import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.BaseResponseModel
import com.github.kieuthang.login_chat.data.entity.UserModel
import io.reactivex.Observable


interface IDataRepository {
    fun login(email: String, password: String): Observable<AccessToken>

    fun getMyProfile(pullToRefresh: Boolean): Observable<UserModel>

    fun register(firstName: String, lastName: String, email: String, password: String): Observable<BaseResponseModel>
}