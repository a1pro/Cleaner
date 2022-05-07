package com.applications.cleaner.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.applications.cleaner.Login_Activity
import com.applications.cleaner.R
import com.applications.cleaner.Shareprefrence.My_Sharepreferences

class Profile_Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_profile_, container, false)
        val sharedPreferences =  My_Sharepreferences(requireContext())
        val logout= view.findViewById<Button>(R.id.logout)
        logout.setOnClickListener{
            sharedPreferences.setlogin(null)
            val ttt=sharedPreferences.getlogin()
            Log.e("ttt", "onCreateView: "+ ttt)
            val intent = Intent(requireContext(), Login_Activity::class.java)
            startActivity(intent)
            activity?.finish()

        }
        return view
    }
}