package com.applications.cleaner

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import org.w3c.dom.Text

class PaymentMethodActivity : AppCompatActivity() {

    private var order_id: String? = ""
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_method)
       
        initIds()
    }

    private fun initIds() {
        order_id = intent!!.getStringExtra("order_id")
        val singleOven =  intent!!.getStringExtra("singleOven")
        val totalAmout =  intent!!.getStringExtra("totalAmout")

        order_no= findViewById(R.id.order_no);
        cleantype= findViewById(R.id.cleantype);
        tv_pending_amount= findViewById(R.id.tv_pending_amount);
        total_amount= findViewById(R.id.total_amount);
        addons= findViewById(R.id.addons);
        tv_paid= findViewById(R.id.tv_paid);

        order_no.text=order_id
        //cleantype.text=singleOven
        tv_pending_amount.text="$0"
        total_amount.text="$"+totalAmout
        tv_paid.text="$0"
        tv_select_an_item = findViewById(R.id.tv_select_an_item);
        ll_payment_type = findViewById(R.id.ll_payment_type);
        tv_cash = findViewById(R.id.tv_cash);
        tv_card = findViewById(R.id.tv_card);
        tv_terminal = findViewById(R.id.tv_terminal);

        tv_select_an_item.setOnClickListener {
            if (ll_payment_type.visibility == View.VISIBLE)
            {
                ll_payment_type.visibility = View.GONE
            }
            else {
                ll_payment_type.visibility = View.VISIBLE
            }

        }
        tv_card.setOnClickListener {
            val intent = Intent(
                Intent(this, PayemntWebview::class.java)
                    .putExtra("order_id", order_id))
            paymentMethodActivityResult.launch(intent)
        }

    }

    var paymentMethodActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
           setResult(RESULT_OK,Intent().putExtra("completeOrder",true))
            finish()
        }
    }
}