package com.github.kieuthang.login_chat.views.common

import com.github.kieuthang.login_chat.data.entity.AccessTokenResponseModel
import com.github.kieuthang.login_chat.data.entity.RoomResponseModel
import com.github.kieuthang.login_chat.data.entity.RoomsResponseModel
import com.github.kieuthang.login_chat.data.entity.UserResponseModel

interface IDataLoadView : BaseContract.View {
    fun onLoginResult(accessToken: AccessTokenResponseModel?)

    fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?)

    fun onRegisterResult(t: AccessTokenResponseModel?, throwable: Throwable?)

    fun onGetRoomsResult(t: RoomsResponseModel?, throwable: Throwable?)

    fun onAddRoomResult(t: RoomResponseModel?, throwable: Throwable?)
}
