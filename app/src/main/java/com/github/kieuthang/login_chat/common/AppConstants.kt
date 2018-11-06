package com.github.kieuthang.login_chat.common


object AppConstants {
    @JvmField
    val TAG = "Socket.Chat_Android"
    val SUPPORTED_MAIL = "kieuducthang@gmail.com"
    @JvmField
    val DEBUG_MODE = true

    object Prefs {
        @JvmField
        val DEFAULT_PREFS = "gsa_prefs"
        @JvmField
        val KEY_EXPIRATION_TIME = "expiration_time"
        @JvmField
        val KEY_JSON_DATA = "json_data"

        @JvmField
        val LAST_TIME_REFRESH_TOKEN = "last_time_refresh_token"
    }

    object Cache {
        @JvmField
        val USER_MODEL = "User_Model"
        @JvmField
        val ACCESS_TOKEN = "access_token"
    }

    object APICodeResponse {
        @JvmField
        val SUCCESS = 0
        @JvmField
        val FAIL = 1
        @JvmField
        val USER_DOES_NOT_EXISTED = 3
        @JvmField
        val CODE_ERROR_401 = 401
    }

}