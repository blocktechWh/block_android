package com.blocktechwh.app.block.Utils;

import android.content.Context;

import com.blocktechwh.app.block.Common.Config;

/**
 * Created by eagune on 2017/11/2.
 */
public class PreferencesUtils {

    public static boolean putInt(Context context,String key, int value) {
        boolean flag = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
        return flag;
    }

    public static int getInt(Context context,String key, int defValue) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static boolean putLong(Context context,String key, long value) {
        boolean flag = context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putLong(key, value).commit();
        return flag;
    }

    public static long getLong(Context context,String key, long defValue) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public static boolean putString(Context context,String key, String value) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putString(key, value).commit();
    }

    public static String getString(Context context,String key, String defValue) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).getString(key, defValue);
    }


    public static boolean putBoolean(Context context,String key, boolean value) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context,String key, boolean defValue) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static boolean clearAllValue(Context context) {
        return context.getSharedPreferences(Config.PREFERENCE_NAME, Context.MODE_PRIVATE).edit().clear().commit();
    }
}
