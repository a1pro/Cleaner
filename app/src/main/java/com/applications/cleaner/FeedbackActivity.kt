package com.applications.cleaner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.NestedScrollView
import com.applications.DownloadListener
import com.applications.cleaner.Models.Orders_
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.applications.cleaner.utils.CommonUtils
import com.github.gcacace.signaturepad.views.SignaturePad
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream


class FeedbackActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: My_Sharepreferences
    var signBitmap: Bitmap? = null
    private var order_id: String? = ""
    private var cleaner_id: String? = ""
    var expe: String? = "worse"
    var expa: String? = "worse"
    var rate: String? = "worse"
    var grease: String? = "no"
    var carbon: String? = "no"
    var protect: String? = "yes"
    var rl_sign: RelativeLayout? = null
    var nestedScroll: NestedScrollView? = null
    var signature_pad: SignaturePad? = null
    var rb_exp_worse: RadioButton? = null
    var rb_exp_bad: RadioButton? = null

    var photo: File? = null
    var rb_exp_ok: RadioButton? = null
    var rb_exp_good: RadioButton? = null
    var rb_exp_best: RadioButton? = null
    var rb_expa_worse: RadioButton? = null
    var rb_expa_bad: RadioButton? = null
    var rb_expa_ok: RadioButton? = null
    var rb_expa_good: RadioButton? = null
    var rb_rate_worse: RadioButton? = null
    var rb_rate_bad: RadioButton? = null
    var rb_rate_ok: RadioButton? = null
    var rb_rate_good: RadioButton? = null
    var rb_rate_best: RadioButton? = null
    var rb_protect_yes: RadioButton? = null

    var rb_protect_no: RadioButton? = null
    var rb_grease_left_yes: RadioButton? = null
    var rb_grease_left_no: RadioButton? = null
    var rb_carbon_yes: RadioButton? = null
    var rb_carbon_no: RadioButton? = null
    var rb_expa_best: RadioButton? = null
    var tv_clear: TextView? = null
    var tv_save: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
        order_id = intent.getStringExtra("order_id")
        cleaner_id = intent.getStringExtra("cleaner_id")
        initdata()
    }

    private fun initdata() {
        sharedPreferences= My_Sharepreferences(this)
        tv_save = findViewById(R.id.tv_save)
        tv_clear = findViewById(R.id.tv_clear)
        rl_sign = findViewById(R.id.rl_sign)
        nestedScroll = findViewById(R.id.nestedScroll)
        signature_pad = findViewById(R.id.signature_pad)
        rb_exp_worse = findViewById(R.id.rb_exp_worse)
        rb_exp_bad = findViewById(R.id.rb_exp_bad)
        rb_exp_ok = findViewById(R.id.rb_exp_ok)
        rb_exp_good = findViewById(R.id.rb_exp_good)
        rb_exp_best = findViewById(R.id.rb_exp_best)
        rb_expa_worse = findViewById(R.id.rb_expa_worse)
        rb_expa_bad = findViewById(R.id.rb_expa_bad)
        rb_expa_ok = findViewById(R.id.rb_expa_ok)
        rb_expa_good = findViewById(R.id.rb_expa_good)
        rb_rate_worse = findViewById(R.id.rb_rate_worse)
        rb_rate_bad = findViewById(R.id.rb_rate_bad)
        rb_rate_ok = findViewById(R.id.rb_rate_ok)
        rb_rate_good = findViewById(R.id.rb_rate_good)
        rb_rate_best = findViewById(R.id.rb_rate_best)
        rb_protect_yes = findViewById(R.id.rb_protect_yes)
        rb_protect_no = findViewById(R.id.rb_protect_no)
        rb_grease_left_yes = findViewById(R.id.rb_grease_left_yes)
        rb_grease_left_no = findViewById(R.id.rb_grease_left_no)
        rb_carbon_yes = findViewById(R.id.rb_carbon_yes)
        rb_carbon_no = findViewById(R.id.rb_carbon_no)
        rb_expa_best = findViewById(R.id.rb_expa_best)
        tv_clear = findViewById(R.id.tv_clear)
        var tv_submit = findViewById<TextView>(R.id.tv_submit)
        tv_submit.setOnClickListener {
            //submitFeedback();
            nestedScroll!!.visibility = View.GONE
            showSignatureView();
        }
        tv_save!!.setOnClickListener {
            //submitFeedback();
            signBitmap = signature_pad!!.signatureBitmap
            // submitFeedback()

            CommonUtils.initProgressDialog(this)
            if (addJpgSignatureToGallery(signBitmap!!)){
                submitFeedback()
            }else
                CommonUtils.hideProgressDialog()

        }
        tv_clear!!.setOnClickListener {
            //submitFeedback();
            signature_pad!!.clear()
            signBitmap = null
            photo = null
        }

    }

    private fun showSignatureView() {
        rl_sign!!.visibility = View.VISIBLE
        signature_pad!!.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
            }

            override fun onSigned() {

            }

            override fun onClear() {
                //Event triggered when the pad is cleared
            }
        })
    }

    private fun submitFeedback() {
        if (rb_exp_worse!!.isChecked)
            expe = "1"
        if (rb_exp_bad!!.isChecked)
            expe = "1"
        if (rb_exp_ok!!.isChecked)
            expe = "1"
        if (rb_exp_good!!.isChecked)
            expe = "1"
        if (rb_exp_best!!.isChecked)
            expe = "1"

        if (rb_expa_worse!!.isChecked)
            expa = "1"
        if (rb_expa_bad!!.isChecked)
            expa = "1"
        if (rb_expa_ok!!.isChecked)
            expa = "1"
        if (rb_expa_good!!.isChecked)
            expa = "1"
        if (rb_expa_best!!.isChecked)
            expa = "1"
        if (rb_rate_worse!!.isChecked)
            rate = "1"
        if (rb_rate_bad!!.isChecked)
            rate = "1"
        if (rb_rate_ok!!.isChecked)
            rate = "1"
        if (rb_rate_good!!.isChecked)
            rate = "1"
        if (rb_rate_best!!.isChecked)
            rate = "1"

        if (rb_protect_no!!.isChecked)
            protect = "1"
        if (rb_protect_yes!!.isChecked)
            protect = "1"


        if (rb_grease_left_no!!.isChecked)
            grease = "1"
        if (rb_grease_left_yes!!.isChecked)
            grease = "1"


        if (rb_carbon_no!!.isChecked)
            carbon = "1"
        if (rb_carbon_yes!!.isChecked)
            carbon = "1"

        val cleanerIdRb: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            sharedPreferences.getlogin()!! + ""
        )
        val requestBody =
            RequestBody.create("*/*".toMediaTypeOrNull(), photo!!)

        val filePart1 = MultipartBody.Part.createFormData(
            "signature",
            photo!!.name,
            requestBody
        )
        val expRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), expe.toString())
        val expaRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), expa.toString())
        val rateRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), rate.toString())
        val protectRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), protect.toString())
        val greaseRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), grease.toString())
        val carbonRb: RequestBody =
            RequestBody.create(("text/plain").toMediaTypeOrNull(), carbon.toString())
        val bookingIdRb: RequestBody = RequestBody.create(
            ("text/plain").toMediaTypeOrNull(),
            order_id.toString()
        )

        CommonUtils.initProgressDialog(this)
        RetrofitClient.instance.submitFeedback(
            expRb, expaRb, rateRb, protectRb,
            greaseRb, carbonRb, filePart1,
            cleanerIdRb,
            bookingIdRb
        )
            ?.enqueue(object : Callback<Orders_?> {
                override fun onResponse(call: Call<Orders_?>, response: Response<Orders_?>) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@FeedbackActivity,
                            "Feedback submitted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()
                    }
                }

                override fun onFailure(call: Call<Orders_?>, t: Throwable) {
                    CommonUtils.hideProgressDialog()
                    Toast.makeText(this@FeedbackActivity,t.printStackTrace().toString(),Toast.LENGTH_LONG).show()
                }

            })

    }

    private var outputDirForDownload =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath


    fun addJpgSignatureToGallery(signature: Bitmap?): Boolean {
        var result = false
        try {
             photo = File(
                getAlbumStorageDir("SignaturePad"),
                String.format("Signature_%d.jpg", System.currentTimeMillis())
            )
            saveBitmapToJPG(signature!!, photo)
            scanMediaFile(photo!!)
            result = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }


    fun getAlbumStorageDir(albumName: String?): File? {
        // Get the directory for the user's public pictures directory.
        val file = File(
            Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
            ), albumName
        )
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created")
        }
        return file
    }

    private fun scanMediaFile(photo: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val contentUri: Uri = Uri.fromFile(photo)
        mediaScanIntent.data = contentUri
        this.sendBroadcast(mediaScanIntent)
    }

    @Throws(IOException::class)
    fun saveBitmapToJPG(bitmap: Bitmap, photo: File?) {
        val newBitmap = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream: OutputStream = FileOutputStream(photo)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }
}