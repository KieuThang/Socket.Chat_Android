package com.github.kieuthang.login_chat.data.entity


class AccessToken: BaseResponseModel() {
    var token: String? = null
    var isActive: Boolean = true
    var isDeleted: Boolean = false
}