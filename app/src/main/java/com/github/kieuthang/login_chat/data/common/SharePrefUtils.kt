package com.github.kieuthang.login_chat.data.common

import android.content.Context
import org.json.JSONArray
import org.json.JSONException
import java.util.*

object SharePrefUtils {

    fun putBooleanValue(context: Context, preference: String, key: String, value: Boolean) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBooleanValue(context: Context, preference: String, key: String): Boolean {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getBoolean(key, false)
    }

    fun getIntValue(context: Context, preference: String, key: String): Int {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getInt(key, 0)
    }

    fun getIntValue(context: Context, preference: String, key: String, defaultValue: Int): Int {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getInt(key, defaultValue)
    }

    fun putIntValue(context: Context, preference: String, key: String, value: Int) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getLongValue(context: Context, preference: String, key: String): Long {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getLong(key, 0)
    }

    fun getLongValue(context: Context, preference: String, key: String, defaultValue: Long): Long {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getLong(key, defaultValue)
    }

    fun putLongValue(context: Context, preference: String, key: String, value: Long) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun putStringValue(context: Context, preference: String, key: String, value: String?) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getStringValue(context: Context, preference: String, key: String): String {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getString(key, "")
    }

    fun getStringValue(context: Context, preference: String, key: String, defaultValue: String): String {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return pref.getString(key, defaultValue)
    }

    fun putStringSetValue(context: Context, preference: String, key: String, values: ArrayList<String>) {
        val set = HashSet(values)
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putStringSet(key, set)
        editor.apply()
    }

    fun getStringSetValue(context: Context, preference: String, key: String): ArrayList<String> {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        return ArrayList(pref.getStringSet(key, HashSet())!!)
    }

    fun putLongArrayValue(context: Context, preference: String, key: String, values: ArrayList<Long>?) {
        val prefs = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val jArr = JSONArray()
        if (values != null && values.size != 0) {
            for (i in values.indices) {
                jArr.put(values[i])
            }
            editor.remove(key)
            editor.putString(key, jArr.toString())
            editor.apply()
        }
    }

    fun getLongArrayValue(context: Context, preference: String, key: String): ArrayList<Long> {
        val prefs = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val json = prefs.getString(key, null)
        val list = ArrayList<Long>()
        if (json != null) {
            try {
                val jArr = JSONArray(json)
                for (i in 0 until jArr.length()) {
                    val item = jArr.optLong(i)
                    list.add(item)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }

        }
        return list
    }

    fun removeKey(context: Context, preference: String, key: String) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
    }

    fun removeSharedPref(context: Context, preference: String) {
        val pref = context.getSharedPreferences(preference, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

}
