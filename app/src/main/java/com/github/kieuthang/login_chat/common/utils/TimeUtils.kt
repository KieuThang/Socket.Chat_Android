package com.github.kieuthang.login_chat.common.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimeUtils {
    fun convertDateTime(drawTime: String?): Long {
        val calendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        return try {
            calendar.time = simpleDateFormat.parse(drawTime)
            calendar.timeInMillis
        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun convertAlarmChatDateTime(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm, dd.MM.yyyy")
        return simpleDateFormat.format(Date(time))
    }

    @SuppressLint("SimpleDateFormat")
    fun convertChatItemDateTime(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy - HH:mm")
        return simpleDateFormat.format(Date(time))
    }

    @SuppressLint("SimpleDateFormat")
    fun getGuardLastLoggedPosition(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(Date(time))
    }

    fun getLastLoggedTime(time: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = time
        val hour: String = if (calendar.get(Calendar.HOUR) > 9) "" + calendar.get(Calendar.HOUR) else "0" + calendar.get(Calendar.HOUR)
        val minute = if (calendar.get(Calendar.MINUTE) > 9) "" + calendar.get(Calendar.MINUTE) else "0" + calendar.get(Calendar.MINUTE)
        val second = if (calendar.get(Calendar.SECOND) > 9) "" + calendar.get(Calendar.SECOND) else "0" + calendar.get(Calendar.SECOND)
        return String.format("%s:%s:%s", hour, minute, second)
    }
}