package com.github.kieuthang.login_chat.data.entity

class Message {
    var createdById: Long = 0
    var createdByName: String? = null
    var message: String? = null
    var sentOn: Long = 0
    var sentByMe: Boolean = false

    var type:Int = 0

    companion object {
        val TYPE_SERVER_CONNECT = 1
        val TYPE_LOADING_VIEW = 2
    }
}