package com.github.kieuthang.login_chat.data.entity

import com.google.gson.annotations.SerializedName


class RoomsResponseModel : BaseResponseModel() {
    @SerializedName("data")
    var rooms: ArrayList<RoomModel>? = null
}