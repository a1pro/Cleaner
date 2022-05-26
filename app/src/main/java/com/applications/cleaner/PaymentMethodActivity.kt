package com.applications.cleaner

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.applications.cleaner.Models.Order_Data
import com.applications.cleaner.Models.Orders_models
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.applications.cleaner.utils.CommonUtils
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentMethodActivity : AppCompatActivity() {

    private var order_id: String? = ""
    private var pendingAmount: String? = ""
    private lateinit var ll_payment_type: LinearLayout
    private lateinit var tv_select_an_item: TextView
    private lateinit var tv_cash: TextView
    private lateinit var tv_card: TextView
    private lateinit var tv_terminal: TextView
    private lateinit var order_no: TextView
    private lateinit var cleantype: TextView
    private lateinit var tv_pending_amount: TextView
    private lateinit var total_amount: TextView
    private lateinit var addons: TextView
    private lateinit var tv_paid: TextView
    private lateinit var data: Order_Data
    private lateinit var sharedPreferences: My_Sharepreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
        sharedPreferences = My_Sharepreferences(this)

        initIds()
    }

    private fun initIds() {
        order_id = intent!!.getStringExtra("order_id")
        val singleOven = intent!!.getStringExtra("singleOven")
        val totalAmout = intent!!.getStringExtra("totalAmout")
        data = intent.getSerializableExtra("data") as Order_Data

        order_no = findViewById(R.id.order_no);
        cleantype = findViewById(R.id.cleantype);
        tv_pending_amount = findViewById(R.id.tv_pending_amount);
        total_amount = findViewById(R.id.total_amount);
        addons = findViewById(R.id.addons);
        tv_paid = findViewById(R.id.tv_paid);

        order_no.text = order_id
        //cleantype.text=singleOven
        tv_pending_amount.text = "$0"
        total_amount.text = "$" + totalAmout
        tv_paid.text = "$0"
        tv_select_an_item = findViewById(R.id.tv_select_an_item);
        ll_payment_type = findViewById(R.id.ll_payment_type);
        tv_cash = findViewById(R.id.tv_cash);
        tv_card = findViewById(R.id.tv_card);
        tv_terminal = findViewById(R.id.tv_terminal);

        tv_select_an_item.setOnClickListener {
            if (ll_payment_type.visibility == View.VISIBLE) {
                ll_payment_type.visibility = View.GONE
            } else {
                ll_payment_type.visibility = View.VISIBLE
            }

        }
        tv_card.setOnClickListener {
            val intent = Intent(
                Intent(this, PayemntWebview::class.java)
                    .putExtra("order_id", order_id)
            )
            paymentMethodActivityResult.launch(intent)
        }
        tv_cash.setOnClickListener {

            callCashPayment();
        }
        tv_terminal.setOnClickListener {

            showAddTransactionIdPopup();
        }
        if (data.paidAmount != null && !data.paidAmount.toString().isEmpty()) {
            var pMAmount = (data.totalAmout.toString().toDouble() - data.paidAmount.toString()
                .toDouble()).toString() as String
            pendingAmount = pMAmount;
            tv_pending_amount.text = pendingAmount
        } else {
            pendingAmount = data.totalAmout.toString()

        }
        tv_pending_amount.text = pendingAmount
    }

    private fun showAddTransactionIdPopup() {

        val dialog = this?.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dailog_add_transaction_id)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val tv_submit = dialog?.findViewById<TextView>(R.id.tv_submit)
        val et_otp = dialog?.findViewById<EditText>(R.id.et_otp)


        tv_submit?.setOnClickListener { view ->
            if (!et_otp!!.text.toString().isEmpty()) {
                dialog!!.dismiss()
                callTerminalPaymentAPI(et_otp!!.text.toString())
            }

        }
        dialog?.show()
    }

    private fun callTerminalPaymentAPI(transactionId: String) {

        CommonUtils.initProgressDialog(this)
        RetrofitClient.instance.terminalPayment(
            pendingAmount!!,
            sharedPreferences.getlogin()!!,
            order_id.toString(),
            transactionId,
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
                        setResult(RESULT_OK, Intent().putExtra("completeOrder", true))
                        finish()
                    }

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })

    }

    private fun callCashPayment() {


        CommonUtils.initProgressDialog(this)
        RetrofitClient.instance.cashPayment(
            pendingAmount!!,
            sharedPreferences.getlogin()!!,
            order_id.toString(),
            data.singleOven.toString(),
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
                        setResult(RESULT_OK, Intent().putExtra("completeOrder", true))
                        finish()
                    }

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })

    }

    var paymentMethodActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            setResult(RESULT_OK, Intent().putExtra("completeOrder", true))
            finish()
        }
    }
}