package com.github.kieuthang.login_chat.common.log;

import android.util.Log;

import java.util.Locale;

public class AppLog {
    public static boolean ASSERT = false;

    /**
     * Customize the log tag for your application, so that other apps
     * using Volley don't mix their logs with yours. <br />
     * Enable the log property for your tag before starting your app: <br />
     * {@code adb shell setprop log.tag.&lt;tag&gt;}
     */
    public static void setTag(String tag) {
        d("Changing log tag to %s", tag);
    }

    public static void v(String format, String args) {
        if (AppConstants.DEBUG_MODE) {
            String message = buildMessage(args);
            Log.v(AppConstants.TAG, message);
        }
    }

    public static void d(String args) {
        d(AppConstants.TAG, args);
    }

    public static void d(String tag, String args) {

        String message = buildMessage(args);
        Log.d(tag, message);
    }

    public static void e(String tag, String args) {

        String message = buildMessage(args);
        Log.e(tag, message);
    }

    public static void e(Throwable tr, String tag, String args) {

        String message = buildMessage(args);
        Log.e(tag, message, tr);
    }

    public static void asserting(Exception ex, String message) {
        if (AppConstants.DEBUG_MODE) {
            String msg = buildMessage(message);
            Exception testEx = ex;
            if (testEx == null)
                testEx = new Exception(message);

            e(testEx.toString(), msg);
        }

        if (ASSERT) {
            throw new IllegalStateException(message);
        }
    }

    /**
     * Formats the caller's provided message and prepends useful info like
     * calling thread ID and method name.
     */
    private static String buildMessage(String args) {
        String msg = args;
        StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

        String caller = "<unknown>";
        for (int i = 2; i < trace.length; i++) {
            Class<?> clazz = trace[i].getClass();
            if (!clazz.equals(AppLog.class)) {
                String callingClass = trace[i].getClassName();
                callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
                callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

                caller = callingClass + "." + trace[i].getMethodName();
                break;
            }
        }
        return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
    }
}
