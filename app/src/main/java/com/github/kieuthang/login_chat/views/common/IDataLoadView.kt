package com.github.kieuthang.login_chat.views.common

import com.github.kieuthang.login_chat.data.entity.*

interface IDataLoadView : BaseContract.View {
    fun onLoginResult(accessToken: AccessTokenResponseModel?)

    fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?)

    fun onRegisterResult(t: AccessTokenResponseModel?, throwable: Throwable?)

    fun onGetRoomsResult(t: RoomsResponseModel?, throwable: Throwable?)

    fun onAddRoomResult(t: RoomResponseModel?, throwable: Throwable?)

    fun onGetChatHistoryResult(t: MessagesResponseModel?, throwable: Throwable?)
}
