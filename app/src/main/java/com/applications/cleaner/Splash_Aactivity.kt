package com.applications.cleaner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.applications.cleaner.Shareprefrence.My_Sharepreferences

class Splash_Aactivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash__aactivity)
        val sharedPreferences =  My_Sharepreferences(this)

        Handler(Looper.getMainLooper()).
        postDelayed({
            if (sharedPreferences.getlogin()!=null) {
                Log.e("getid", "onCreate: "+sharedPreferences.getlogin() )
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            else{
                Log.e("getid", "onCreate: "+sharedPreferences.getlogin() )
                val intent = Intent(this, Login_Activity::class.java)
                startActivity(intent)
                finish()
            }
        }, 5000)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }


}