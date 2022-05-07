package com.applications.cleaner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Adaptors.AddonAdaptor
import com.applications.cleaner.Models.AddOnsList
import com.applications.cleaner.Models.Addon_model
import com.applications.cleaner.Models.DataAddOns
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.utils.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOnsActivity : AppCompatActivity() {
    private lateinit var skip: AppCompatButton
    private lateinit var list: ArrayList<DataAddOns>

    private lateinit var addon_recycler: RecyclerView
    private lateinit var Add_to_order: Button

    private lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ons)
        list = arrayListOf<DataAddOns>()
        skip = findViewById(R.id.skip)
        Add_to_order = findViewById(R.id.Add_to_order)
        Add_to_order.setOnClickListener {


        }


        addon_recycler = findViewById(R.id.addon_recycler)
       setAdapter()
        skip.setOnClickListener {
            startActivity(Intent(this, UploadPhotoActivity::class.java))
        }

        callGetProductsList();
    }

    private fun setAdapter() {
        addon_recycler.adapter = AddonAdaptor(list)
        addon_recycler.layoutManager = LinearLayoutManager(this)

    }

    private fun callGetProductsList() {

        CommonUtils.initProgressDialog(this)
        RetrofitClient.instance.orderListOrAddOns()
            .enqueue(object : Callback<AddOnsList> {

                override fun onResponse(call: Call<AddOnsList>, response: Response<AddOnsList>) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {

                        list.addAll(response.body()!!.data!!)
                        setAdapter()

                    }
                }


                override fun onFailure(call: Call<AddOnsList>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }



}