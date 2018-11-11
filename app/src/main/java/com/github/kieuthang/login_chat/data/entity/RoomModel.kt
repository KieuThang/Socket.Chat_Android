package com.github.kieuthang.login_chat.data.entity

import java.io.Serializable


class RoomModel: Serializable {
    var id: Long = 0
    var name: String? = null
    var created: String? = null
    var createdById: Int = 0
    var createdByName: String? = null
}