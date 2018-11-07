package com.github.kieuthang.login_chat.data.entity

import com.google.gson.annotations.SerializedName


class UserResponseModel : BaseResponseModel() {
    @SerializedName("data")
    var userModel: UserModel? = null
}