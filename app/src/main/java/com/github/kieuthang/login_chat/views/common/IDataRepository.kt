package com.github.kieuthang.login_chat.views.common

import com.github.kieuthang.login_chat.data.entity.*
import io.reactivex.Observable


interface IDataRepository {
    fun login(email: String, password: String): Observable<AccessTokenResponseModel>

    fun getMyProfile(pullToRefresh: Boolean): Observable<UserResponseModel>

    fun register(firstName: String, lastName: String, email: String, password: String): Observable<AccessTokenResponseModel>

    fun getRooms(): Observable<RoomsResponseModel>

    fun addRoom(name: String): Observable<RoomResponseModel>

    fun getChatHistory(id: Long): Observable<MessagesResponseModel>
}