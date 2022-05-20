package com.applications.cleaner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.Toast.LENGTH_LONG
import com.applications.cleaner.Models.Login
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.applications.cleaner.utils.CommonUtils
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

class Login_Activity : AppCompatActivity() {


    private lateinit var phone_no_text: EditText
    private lateinit var password_text: EditText
    private lateinit var data: Login


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        val sharedPreferences = My_Sharepreferences(this)


        phone_no_text = findViewById(R.id.phone_no_text)
        password_text = findViewById(R.id.password_text)
        val sign_up = findViewById<TextView>(R.id.sign_up)
        sign_up.setOnClickListener {
            val intent = Intent(this, Signup_Activity::class.java)
            startActivity(intent)
        }
        val login_btn = findViewById<Button>(R.id.login_btn)
        login_btn.setOnClickListener {

            if (phone_no_text.getText().toString().trim().length.equals(0)) {
                Toast.makeText(
                    applicationContext,
                    "Enter Phone Number",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (password_text.getText().toString().trim().length.equals(0)) {
                Toast.makeText(
                    applicationContext,
                    "Enter Password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                try {
                    CommonUtils.initProgressDialog(this)
                    Log.e("login", "1: ")
                    Log.e("login", "1: " + phone_no_text.text.toString())
                    Log.e("login", "1: " + password_text.text.toString())
                    RetrofitClient.instance.login(
                        phone_no_text.text.toString(),
                        password_text.text.toString()
                    )

                        .enqueue(object : retrofit2.Callback<Login> {

                            override fun onResponse(call: Call<Login>, response: Response<Login>) {
                                CommonUtils.hideProgressDialog()
                                if (response.isSuccessful) {
                                    data = response.body()!!

                                    Log.e("login", "onResponse: " + data.code)
                                    Log.e("login", "onResponse:1111 " + data.status)
                                    if (data.code!!.equals(201)) {
                                        sharedPreferences.setlogin(data.data.get(0).id.toString())
                                        sharedPreferences.setusername(data.data.get(0).name.toString())
                                        Log.e("asd", "on " + data.data.get(0).id)

                                        Toast.makeText(
                                            applicationContext,
                                            "Login Successful",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        val intent =
                                            Intent(applicationContext, MainActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                        Log.e("login", "fhfhfj: " + data.code)
                                    } else {
                                        Log.e("login", "fhfhfj: " + data.status)
                                        Toast.makeText(
                                            applicationContext,
                                            "" + data.status,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                            }


                            override fun onFailure(call: Call<Login>, t: Throwable) {
                                CommonUtils.hideProgressDialog()
                                Log.e("login", "onFailure: " + t)
                                Toast.makeText(baseContext, "" + t, LENGTH_LONG)


                            }

                        })


                } catch (e: Exception) {
                    Log.e("login", "fhfhtdjhtyjty " + e)
                    Toast.makeText(applicationContext, "fgnfhs " + e, Toast.LENGTH_SHORT).show()

                }
            }
        }


    }
}