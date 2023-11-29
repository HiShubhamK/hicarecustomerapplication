package com.ab.hicareservices.utils

import android.content.Context
import com.google.gson.Gson

class SharedPreferencesManager(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("UserDataPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        private const val USER_DATA_KEY = "user_data"
    }

    fun saveUserData(userData: UserData) {
        val userDataJson = gson.toJson(userData)
        sharedPreferences.edit().putString(USER_DATA_KEY, userDataJson).apply()
    }

    fun getUserData(): UserData? {
        val userDataJson = sharedPreferences.getString(USER_DATA_KEY, null)
        return if (userDataJson != null) {
            gson.fromJson(userDataJson, UserData::class.java)
        } else {
            null
        }
    }

    fun clearUserData() {
        sharedPreferences.edit().remove(USER_DATA_KEY).apply()
    }
}
