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

}