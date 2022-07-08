package com.applications.cleaner

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applications.cleaner.Adaptors.AddonAdaptor
import com.applications.cleaner.Interface.Addon_product
import com.applications.cleaner.Models.AddOnsList
import com.applications.cleaner.Models.DataAddOns
import com.applications.cleaner.Models.Orders_models
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.utils.CommonUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddOnsActivity : AppCompatActivity(),Addon_product {
    private var product_id: String? = ""
    private var order_id: String? = ""
    private var cleaner_id: String? = ""
    private lateinit var prizetext: TextView
    private lateinit var skip: AppCompatButton

    private lateinit var list: ArrayList<DataAddOns>
    private lateinit var addonid: ArrayList<String>
    private var prize: Double?= null
    private var wages_price: Double? = null
    private  var idget:String? =""

    private lateinit var addon_recycler: RecyclerView
    private lateinit var Add_to_order: Button

    private lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_ons)

        product_id = intent.getStringExtra("product_id")
        order_id = intent.getStringExtra("order_id")
        cleaner_id = intent.getStringExtra("cleaner_id")
        list = arrayListOf<DataAddOns>()
        addonid = arrayListOf<String>()
        skip = findViewById(R.id.skip)
        prizetext = findViewById(R.id.prize)

        Add_to_order = findViewById(R.id.Add_to_order)
        prize=0.00
        wages_price=0.00
        Add_to_order.setOnClickListener{

            for (i in addonid.indices) {

                    if (idget!!.isEmpty()) {
                        idget =addonid[i]
                    } else {
                        idget = idget + "," +addonid[i]

                }
            }
            Log.e("addon_data", "onCreate: $idget, $prize , $wages_price")

            addons()
        }


        addon_recycler = findViewById(R.id.addon_recycler)
        setAdapter()
        skip.setOnClickListener {
            //startActivity(Intent(this, UploadPhotoActivity::class.java))
         /*   startActivity(
                Intent(this, UploadPhotoActivity::class.java)
                    .putExtra("order_id", order_id)
                    .putExtra("cleaner_id", cleaner_id)
            )*/
            val intent = Intent(
                Intent(this, UploadPhotoActivity::class.java)
                    .putExtra("product_id", product_id)
                    .putExtra("order_id", order_id)
                    .putExtra("cleaner_id", cleaner_id)
            )
            uploadActivityResult.launch(intent)
        }

        callGetProductsList();
    }

    private fun addons() {
        Log.e("addonresponse", "onCreate:$cleaner_id , $order_id ,  $idget , $prize , $wages_price")

        CommonUtils.initProgressDialog(baseContext)
        RetrofitClient.instance.addonUpsellby(
            cleaner_id.toString()!!, order_id.toString()!!,idget.toString()!!,prize.toString()!!,wages_price.toString()!!
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful && response.body() != null && response!!.body()!!.code!!.equals(
                            201
                        )
                    ) {
                      if (response.isSuccessful){
                          val model = response.body()
                          Log.e("addonresponse", "onResponse: "+model?.status )
                          val intent = Intent(
                              Intent(baseContext, UploadPhotoActivity::class.java)
                                  .putExtra("product_id", product_id)
                                  .putExtra("order_id", order_id)
                                  .putExtra("cleaner_id", cleaner_id)
                          )
                          uploadActivityResult.launch(intent)
                      }
                    }

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    var uploadActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }

    private fun setAdapter() {
        addon_recycler.adapter = AddonAdaptor(list,this)
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

    override fun product(id: String, prize_adp: Double, wages_price_adp: Double) {

        addonid.add(id)
        prize= prize!!+ prize_adp
        prizetext.setText(prize.toString())
        wages_price= wages_price!! + wages_price_adp

    }

    override fun product_dissmiss(id: String, prize_adp: Double, wages_price_adp: Double) {
        idget=""
        addonid.remove(id)
        prize=prize!! - prize_adp
        prizetext.setText(prize.toString())
        wages_price= wages_price!! - wages_price_adp
    }


}