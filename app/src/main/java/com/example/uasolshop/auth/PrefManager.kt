package com.example.uasolshop.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class PrefManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences
    companion object {
        private const val PREFS_FILENAME = "AuthAppPrefs"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_ROLE = "admin"
        @Volatile
        private var instance: PrefManager? = null
        fun getInstance(context: Context): PrefManager {
            return instance ?: synchronized(this) {
                instance ?: PrefManager(context.applicationContext).also {
                    instance = it
                }
            }
        }
    }
    init {
        sharedPreferences = context.getSharedPreferences(
            PREFS_FILENAME,
            Context.MODE_PRIVATE)
    }
    fun setLoggedIn(isLoggedIn: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn)
        editor.apply()
    }
    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    fun saveUsername(username: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply()
    }
    fun savePassword(password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }
    fun saveRole(role: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_ROLE, role)
        editor.apply()
    }
    fun getUsername(): String {
        Log.d("usrnem", KEY_USERNAME)
        return sharedPreferences.getString(KEY_USERNAME, "admin") ?: ""
    }
    fun getPassword(): String {
//        Log.d("pass", return sharedPreferences.getString(KEY_PASSWORD, "") ?: "")
        return sharedPreferences.getString(KEY_PASSWORD, "admin123") ?: ""
    }
    fun getRole(): String {
        return sharedPreferences.getString(KEY_ROLE, "admin") ?: ""
    }
    fun clear() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}