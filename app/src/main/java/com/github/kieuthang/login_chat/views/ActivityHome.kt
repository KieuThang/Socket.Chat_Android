package com.github.kieuthang.login_chat.views

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.views.common.BaseFragmentActivity

class ActivityHome: BaseFragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

    }

    companion object {
        fun createIntent(context: Context): Intent {
            return Intent(context, ActivityHome::class.java)
        }
    }
}