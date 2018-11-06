package com.github.kieuthang.login_chat.views.common


import com.github.kieuthang.login_chat.data.entity.AccessToken
import com.github.kieuthang.login_chat.data.entity.BaseResponseModel
import com.github.kieuthang.login_chat.data.entity.UserModel

interface IDataLoadView : BaseContract.View {
    fun onLoginResult(accessToken: AccessToken?)

    fun onGetMyProfileResult(t: UserModel?, throwable: Throwable?)

    fun onRegisterResult(t: BaseResponseModel?, throwable: Throwable?)
}
