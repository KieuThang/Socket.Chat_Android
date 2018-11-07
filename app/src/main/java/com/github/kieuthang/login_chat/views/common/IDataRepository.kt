package com.github.kieuthang.login_chat.views.common

import com.github.kieuthang.login_chat.data.entity.AccessTokenResponseModel
import com.github.kieuthang.login_chat.data.entity.RoomResponseModel
import com.github.kieuthang.login_chat.data.entity.RoomsResponseModel
import com.github.kieuthang.login_chat.data.entity.UserResponseModel
import io.reactivex.Observable


interface IDataRepository {
    fun login(email: String, password: String): Observable<AccessTokenResponseModel>

    fun getMyProfile(pullToRefresh: Boolean): Observable<UserResponseModel>

    fun register(firstName: String, lastName: String, email: String, password: String): Observable<AccessTokenResponseModel>

    fun getRooms(): Observable<RoomsResponseModel>

    fun addRoom(name: String): Observable<RoomResponseModel>
}