package com.github.kieuthang.login_chat.data.entity

import com.google.gson.annotations.SerializedName


class RoomResponseModel : BaseResponseModel() {
    @SerializedName("data")
    var room: RoomModel? = null
}