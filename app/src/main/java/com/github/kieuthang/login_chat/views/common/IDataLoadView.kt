package com.github.kieuthang.login_chat.views.common


import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.UserResponseModel

interface IDataLoadView : BaseContract.View {
    fun onLoginResult(accessToken: AccessToken?)

    fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?)
}
