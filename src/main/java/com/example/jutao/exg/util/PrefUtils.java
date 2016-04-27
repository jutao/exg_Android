package com.example.jutao.exg.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 对SharedPreferences的封装
 */
public class PrefUtils {
  public static Boolean getBoolean(Context context, String key, Boolean defValue) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    return sharedPreferences.getBoolean(key, defValue);
  }

  public static void setBoolean(Context context, String key, Boolean value) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    sharedPreferences.edit().putBoolean(key, value).commit();
  }

  public static String getString(Context context, String key, String defValue) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    return sharedPreferences.getString(key, defValue);
  }

  public static void setString(Context context, String key, String value) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    sharedPreferences.edit().putString(key, value).commit();
  }

  public static int getInt(Context context, String key, int defValue) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    return sharedPreferences.getInt(key, defValue);
  }

  public static void setInt(Context context, String key, int value) {
    SharedPreferences sharedPreferences =
        context.getSharedPreferences("config", context.MODE_PRIVATE);
    sharedPreferences.edit().putInt(key, value).commit();
  }
}
