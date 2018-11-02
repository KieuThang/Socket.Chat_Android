package com.github.kieuthang.login_chat.common.utils


import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.os.PowerManager
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast
import com.github.kieuthang.login_chat.R
import com.github.kieuthang.login_chat.common.AppConstants
import com.github.kieuthang.login_chat.common.log.AppLog
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import java.io.IOException
import java.lang.reflect.Modifier
import java.util.*

object ApplicationUtils {
    private var wakeLock: PowerManager.WakeLock? = null

    fun makeJsonObject(`object`: Any): JsonObject {
        val builder = GsonBuilder()
        builder.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
        val gson = builder.create()
        val json = gson.toJson(`object`)
        val jsonParser = JsonParser()
        return jsonParser.parse(json) as JsonObject
    }

    fun contactUsByMail(context: Context) {
        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:" + AppConstants.SUPPORTED_MAIL)
        emailIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        try {
            context.startActivity(Intent.createChooser(emailIntent, context.getString(R.string.send_mail_using)))
        } catch (ex: android.content.ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.no_email_client_installed), Toast.LENGTH_SHORT).show()
        }
    }

//    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
//        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
//        return manager.getRunningServices(Integer.MAX_VALUE).any { serviceClass.name == it.service.className }
//    }

    fun generateCustomChooserIntent(context: Context, prototype: Intent?): Intent {
        val forbiddenChoices = ArrayList<String>()
        forbiddenChoices.add(AppConstants.GOOLGE_PHOTOS_PACKAGE)
        forbiddenChoices.add(AppConstants.GOOLGE_PLUS_PACKAGE)

        val targetedShareIntents = ArrayList<Intent>()
        val intentMetaInfo = ArrayList<HashMap<String, String>>()
        val chooserIntent: Intent

        val resInfo = context.packageManager.queryIntentActivities(prototype, 0)

        if (!resInfo.isEmpty()) {
            for (resolveInfo in resInfo) {
                AppLog.d(AppConstants.TAG, "packageName:" + resolveInfo.activityInfo.packageName + ",className:" + resolveInfo.activityInfo.name
                        + ",simpleName:" + resolveInfo.activityInfo.loadLabel(context.packageManager).toString())
                if (resolveInfo.activityInfo == null || forbiddenChoices.contains(resolveInfo.activityInfo.packageName))
                    continue
                val simpleName = resolveInfo.activityInfo.loadLabel(context.packageManager).toString()
                val info = HashMap<String, String>()
                info.put("packageName", resolveInfo.activityInfo.packageName)
                info.put("className", resolveInfo.activityInfo.name)
                info.put("simpleName", if (TextUtils.isEmpty(simpleName)) "Crop picture" else simpleName)
                intentMetaInfo.add(info)
            }

            if (!intentMetaInfo.isEmpty()) {

                // create the custom intent list
                for (metaInfo in intentMetaInfo) {
                    val targetedShareIntent = prototype!!.clone() as Intent
                    targetedShareIntent.putExtra("AppName", "Crop picture")
                    targetedShareIntent.putExtra(Intent.EXTRA_TITLE, "Crop picture")
                    targetedShareIntent.`package` = metaInfo["packageName"]
                    targetedShareIntent.setClassName(metaInfo["packageName"], metaInfo["className"])
                    targetedShareIntents.add(targetedShareIntent)
                }

                chooserIntent = Intent.createChooser(targetedShareIntents.removeAt(targetedShareIntents.size - 1), context.getString(R.string.complete_action_using))
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toTypedArray<Parcelable>())
                return chooserIntent
            }
        }

        return Intent.createChooser(prototype, context.getString(R.string.complete_action_using))
    }


    fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun isAppOnForeground(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val appProcesses = activityManager.runningAppProcesses ?: return false
        val packageName = context.packageName
        return appProcesses.any { it.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND && it.processName == packageName }
    }

    fun turnOnCPUWithTimeout(context: Context) {
        val timeout = 10000L
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (wakeLock == null) {
            wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, ApplicationUtils::class.java.simpleName)
            wakeLock!!.acquire(timeout)
        } else if (wakeLock!!.isHeld) {
            wakeLock!!.release()
            wakeLock!!.acquire(timeout)
        } else if (!wakeLock!!.isHeld) {
            wakeLock!!.acquire(timeout)
        }
    }

    fun getJSONData(context: Context, jsonFile: String): String? {
        val json: String
        try {
            val inputStream = context.assets.open(jsonFile)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, charset("UTF-8"))
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

        return json
    }

    fun unlockBrightAndTurnonScreen(context: Activity) {
        turnOnCPUWithTimeout(context)
        unlockKeyBoard(context)
        turnOnScreen(context)
    }

    fun turnOnScreen(context: Activity) {
        val window = context.window
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
    }

    fun turnOffScreen(context: Activity) {
        val window = context.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                or WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)
        if (wakeLock != null && wakeLock!!.isHeld()) {
            wakeLock!!.release()
        }
    }

    fun unlockKeyBoard(context: Context) {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val keyguardLock = keyguardManager.newKeyguardLock("TAG")
        keyguardLock.disableKeyguard()
    }
}
