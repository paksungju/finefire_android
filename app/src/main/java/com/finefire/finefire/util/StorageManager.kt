package com.finefire.finefire.util
import android.content.Context
import android.content.Context.MODE_PRIVATE

class StorageManager {

    val preferencesKey: String = "appData"

    fun getToken(context: Context): String? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getString("token", "")
    }

    fun setToken(context: Context, str: String) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putString("token", str.trim { it <= ' ' })
        editor.apply()
    }


    fun getID(context: Context): String? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getString("id", "")
    }

    fun setID(context: Context, str: String) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putString("id", str.trim { it <= ' ' })
        editor.apply()
    }


    fun getPW(context: Context): String? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getString("pw", "")
    }

    fun setPW(context: Context, str: String) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putString("pw", str.trim { it <= ' ' })
        editor.apply()
    }

    fun getUserId(context: Context): Int? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getInt("user_id", -1)
    }

    fun setUserId(context: Context, str: Int) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putInt("user_id", str)
        editor.apply()
    }

    fun getName(context: Context): String? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getString("name", "")
    }

    fun setName(context: Context, str: String) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putString("name", str.trim { it <= ' ' })
        editor.apply()
    }


    fun getLoginToken(context: Context): String? {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        return appData.getString("login_token", "")
    }

    fun setLoginToken(context: Context, str: String) {
        val appData = context.getSharedPreferences(preferencesKey, MODE_PRIVATE)
        val editor = appData.edit()
        editor.putString("login_token", str.trim { it <= ' ' })
        editor.apply()
    }

}