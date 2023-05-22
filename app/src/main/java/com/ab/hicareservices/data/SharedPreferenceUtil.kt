package com.ab.hicareservices.data

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUtil(context: Context) {
    private var sharedPreferences: SharedPreferences
    private val prefsName = "hcServicesPrefs"
    init {
        sharedPreferences = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
    }

    companion object{
        private fun sharedPref(context: Context): SharedPreferenceUtil = SharedPreferenceUtil(context)

        /**
         * It is used to get any data stored in the App SharedPreference
         * You can set any data without explicitly typing the name of the method
         * */
        fun getData(context: Context, param: String, defaultVal: String): Any?{
            return sharedPref(context).sharedPreferences.getString(param, defaultVal)
        }

        fun getData(context: Context, param: String, defaultVal: Boolean): Any{
            return sharedPref(context).sharedPreferences.getBoolean(param, defaultVal)
        }

        fun getData(context: Context, param: String, defaultVal: Int): Any{
            return sharedPref(context).sharedPreferences.getInt(param, defaultVal)
        }

        fun getData(context: Context, param: String, defaultVal: Float): Any{
            return sharedPref(context).sharedPreferences.getFloat(param, defaultVal)
        }

        fun getData(context: Context, param: String, defaultVal: MutableSet<String>): MutableSet<String>?{
            return sharedPref(context).sharedPreferences.getStringSet(param, defaultVal)
        }

        fun getData(context: Context, param: String, defaultVal: Long): Any{
            return sharedPref(context).sharedPreferences.getLong(param, defaultVal)
        }

        /**
         * It is used to set any data stored in the App SharedPreference
         * You can set any data without explicitly typing the name of the method
        * */
        fun setData(context: Context, key: String?, value: String?){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()
            editor.commit()
        }

        fun setData(context: Context, key: String?, value: Int){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putInt(key, value)
            editor.apply()
            editor.commit()
        }

        fun setData(context: Context, key: String?, value: Boolean){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
            editor.commit()
        }

        fun setData(context: Context, key: String?, value: Float){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putFloat(key, value)
            editor.apply()
            editor.commit()
        }

        fun setData(context: Context, key: String?, value: Long){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putLong(key, value)
            editor.apply()
            editor.commit()
        }

        fun setData(context: Context, key: String?, value: MutableSet<String>){
            val editor: SharedPreferences.Editor = sharedPref(context).sharedPreferences.edit()
            editor.putStringSet(key, value)
            editor.apply()
            editor.commit()
        }
    }
}