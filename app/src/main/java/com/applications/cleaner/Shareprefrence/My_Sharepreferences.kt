package com.applications.cleaner.Shareprefrence

import android.content.Context
import com.applications.cleaner.Fragments.Profile_Fragment
import com.applications.cleaner.Login_Activity
import kotlinx.coroutines.withContext

class My_Sharepreferences(context: Context) {


    val Preferncename = "shareprefernceexample"

    val sharepreferences = context.getSharedPreferences(Preferncename, Context.MODE_PRIVATE)

    fun setBeforeImageUploaded(before: Boolean,orderid:String) {
        val editor = sharepreferences.edit()
        editor.putBoolean(orderid, before)
        editor.apply()
    }

    fun isBeforeImageUploaded(orderid: String): Boolean {
        return sharepreferences.getBoolean(orderid, false)
    }

    fun setlogin(userid: String?) {
        val editor = sharepreferences.edit()
        editor.putString("user_id", userid)
        editor.apply()
    }

    fun getlogin(): String? {
        return sharepreferences.getString("user_id", null)
    }

    fun setusername(userid: String?) {
        val editor = sharepreferences.edit()
        editor.putString("user_name", userid)
        editor.apply()
    }

    fun getusername(): String? {
        return sharepreferences.getString("user_name", null)
    }

}