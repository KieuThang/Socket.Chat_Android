package com.github.kieuthang.login_chat.common.utils


import android.text.TextUtils
import android.util.Patterns

import java.util.regex.Matcher
import java.util.regex.Pattern

object StringUtils {
    private val VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE)

    private fun validate(emailStr: String): Boolean {
        val matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr)
        return matcher.find()
    }

    fun verifyEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && validate(email)
    }

    fun verifyPhoneNumber(phone: String): Boolean {
        return Patterns.PHONE.matcher(phone).matches()
    }

}