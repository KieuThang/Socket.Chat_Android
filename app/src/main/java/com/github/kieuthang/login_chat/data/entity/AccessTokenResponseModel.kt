package com.github.kieuthang.login_chat.data.entity

import com.google.gson.annotations.SerializedName


class AccessTokenResponseModel: BaseResponseModel(){
    @SerializedName("data")
    var accessToken: AccessToken? = null
}