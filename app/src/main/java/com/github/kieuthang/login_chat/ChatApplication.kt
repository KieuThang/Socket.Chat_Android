package com.github.kieuthang.login_chat

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.github.kieuthang.login_chat.data.common.RestApiClient
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class ChatApplication : Application() {

    var socket: Socket? = null
        private set

    init {
        try {
            socket = IO.socket(RestApiClient.BASE_PRODUCTION_URL)
        } catch (e: URISyntaxException) {
            throw RuntimeException(e)
        }

    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
            private set
    }
}
