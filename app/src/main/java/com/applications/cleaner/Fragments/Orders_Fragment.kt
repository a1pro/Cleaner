package com.applications.cleaner.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Adaptors.Order_adaptor
import com.applications.cleaner.Models.Orders_
import com.applications.cleaner.R
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class Orders_Fragment : Fragment() {
    private lateinit var list: ArrayList<Orders_>
    private lateinit var order_list:Orders_
    private var progressBar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_orders_, container, false)

        val sharedPreferences =  My_Sharepreferences(requireContext())
        list = arrayListOf<Orders_>()
        progressBar = view.findViewById(R.id.progress_Bar) as ProgressBar

        val recycler_order=view.findViewById<RecyclerView>(R.id.recycler_order)
        recycler_order.layoutManager= LinearLayoutManager(this.context)

        progressBar!!.visibility = View.VISIBLE
        RetrofitClient.instance.getorderlist(sharedPreferences.getlogin()!!)
            .enqueue(object : Callback<Orders_>{

                override fun onResponse(call: Call<Orders_>, response: Response<Orders_>) {

                    if (response.isSuccessful){
                        progressBar!!.visibility = View.GONE
                        order_list=response.body()!!
                        if (order_list.code!!.equals(201)){
                            Log.e("response", "onResponse: " +response)

                            recycler_order.adapter= Order_adaptor(order_list.data)
                        }

                    }
                }


                override fun onFailure(call: Call<Orders_>, t: Throwable) {
                    Log.e("response", "onResponse: " +t)
                    progressBar!!.visibility = View.GONE
                }

            })







        return view
    }


}




