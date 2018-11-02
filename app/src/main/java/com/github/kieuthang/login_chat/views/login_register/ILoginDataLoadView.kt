package com.github.kieuthang.login_chat.views.login


import com.github.kieuthang.login_chat.data.user.entity.AccessToken
import com.github.kieuthang.login_chat.data.user.entity.BaseResponseModel
import com.github.kieuthang.login_chat.data.user.entity.UserModel
import com.github.kieuthang.login_chat.data.user.entity.UserResponseModel
import com.github.kieuthang.login_chat.views.common.BaseContract

interface ILoginDataLoadView : BaseContract.View {
    fun onLoginResult(accessToken: AccessToken?)
    fun onForgotPWResult(response: BaseResponseModel?)
    fun onGetMyProfileResult(t: UserResponseModel?, throwable: Throwable?)
}
