package com.github.kieuthang.login_chat.data.entity

import com.google.gson.annotations.SerializedName

class MessagesResponseModel : BaseResponseModel() {
    @SerializedName("data")
    var messages: ArrayList<Message>? = null
}