package com.applications.cleaner

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.applications.cleaner.Models.Orders_models
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.applications.cleaner.utils.CommonUtils
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadPhotoActivity : AppCompatActivity() {
    private var product_id: String? = ""
    private var order_id: String? = ""
    private var cleaner_id: String? = ""
    private lateinit var btn_upload: TextView
    private lateinit var iv_image_one: ImageView
    private lateinit var iv_image_two: ImageView
    private lateinit var tv_report_damage: TextView
    private lateinit var tv_photo_count: TextView
    private lateinit var tv_after: TextView
    private val beforeImagesList: ArrayList<File> = ArrayList<File>()
    private lateinit var tv_before: TextView
    private lateinit var sharedPreferences: My_Sharepreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_photo)
        sharedPreferences = My_Sharepreferences(this)
        product_id = intent.getStringExtra("product_id")
        order_id = intent.getStringExtra("order_id")
        cleaner_id = intent.getStringExtra("cleaner_id")
        init()
    }

    fun init() {
        btn_upload = findViewById(R.id.btn_upload)
        iv_image_one = findViewById(R.id.iv_image_one)
        iv_image_two = findViewById(R.id.iv_image_two)
        tv_report_damage = findViewById(R.id.tv_report_damage)
        tv_photo_count = findViewById(R.id.tv_photo_count)
        tv_after = findViewById(R.id.tv_after)
        tv_before = findViewById(R.id.tv_before)

        tv_before.setOnClickListener {
            if (beforeImagesList.size < 2)
                showImageOptionPopup()

        }
        tv_report_damage.setOnClickListener {
            showAddProblemNotesDialog();

        }

        tv_after.setOnClickListener {
            if (beforeImagesList.size < 2)
                showImageOptionPopup()
        }
        btn_upload.setOnClickListener {
            if (beforeImagesList.size == 2) {
                if (tv_before.isEnabled)
                    uploadImagesBeforeCleaning()
                else completeCleaning()
            };
        }

        if (sharedPreferences.isBeforeImageUploaded(order_id!!))
            setAfterCleaningView();

    }
    private fun showAddProblemNotesDialog() {
        val dialog = this@UploadPhotoActivity.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dailog_add_problem_notes)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val tv_submit = dialog?.findViewById<TextView>(R.id.tv_submit)
        val et_notes = dialog?.findViewById<EditText>(R.id.et_notes)

        tv_submit?.setOnClickListener { view ->
            callProblemAPI(et_notes?.text.toString())
            dialog?.dismiss()

        }

        dialog?.show()
    }

    private fun callProblemAPI(note: String) {
        CommonUtils.initProgressDialog(this@UploadPhotoActivity)
        RetrofitClient.instance.haveProblemWithOrder(
            sharedPreferences.getlogin()!!,
            order_id!!,
            note
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@UploadPhotoActivity,
                            "Your problem has been registered successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackPressed()

                    }
                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    private fun completeCleaning() {

        CommonUtils.initProgressDialog(this)
        val cleanerIdRb: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            sharedPreferences.getlogin()!! + ""
        )
        val notesRb: RequestBody = RequestBody.create(("text/plain").toMediaTypeOrNull(), "123")
        val bookingIdRb: RequestBody = RequestBody.create(
            ("text/plain").toMediaTypeOrNull(),
            order_id!!
        )


        val requestBody =
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), beforeImagesList.get(0))
        val requestBody2 =
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), beforeImagesList.get(1))

        val filePart1 = MultipartBody.Part.createFormData(
            "photo_one",
            beforeImagesList.get(0).name,
            requestBody
        )
        val filePart2 = MultipartBody.Part.createFormData(
            "photo_two",
            beforeImagesList.get(1).name,
            requestBody2
        )
        val productIdRB: RequestBody = RequestBody.create(("text/plain").toMediaTypeOrNull(), product_id!!)
        RetrofitClient.instance.completeCleaning(
            cleanerIdRb,
            bookingIdRb,
            productIdRB,
            filePart1,
            filePart2

        )!!.enqueue(object : Callback<Orders_models?> {
            override fun onResponse(call: Call<Orders_models?>, response: Response<Orders_models?>) {
                CommonUtils.hideProgressDialog()
                if (response.isSuccessful && response.body()!!.code == 201) {
                    sharedPreferences.setBeforeImageUploaded(true, order_id!!)
                    Toast.makeText(
                        this@UploadPhotoActivity,
                        "Your order has been completed successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(
                        Intent(this@UploadPhotoActivity, FeedbackActivity::class.java)
                            .putExtra("order_id", order_id)
                            .putExtra("cleaner_id", cleaner_id)
                    )
                    feedbackActivityResult.launch(intent)

                }else{
                    Toast.makeText(
                        this@UploadPhotoActivity,
                        "Something went wrong, please  try again later.",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(call: Call<Orders_models?>, t: Throwable) {
                CommonUtils.hideProgressDialog()
                Toast.makeText(
                    this@UploadPhotoActivity,
                    t.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()

            }

        })

    }

    var feedbackActivityResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val intent = Intent()
        if (result != null && result.data != null && result.data!!.hasExtra("feedbackAdded"))
            intent.putExtra("feedbackAdded", true)
        setResult(RESULT_OK, intent.putExtra("refreshAPI", true))
        finish()
    }

    private fun uploadImagesBeforeCleaning() {

        CommonUtils.initProgressDialog(this)
        val cleanerIdRb: RequestBody = RequestBody.create(
            "text/plain".toMediaTypeOrNull(),
            sharedPreferences.getlogin()!! + ""
        )
        val product_idRB: RequestBody = RequestBody.create(("text/plain").toMediaTypeOrNull(), product_id!!)
        val notesRb: RequestBody = RequestBody.create(("text/plain").toMediaTypeOrNull(), "123")
        val bookingIdRb: RequestBody = RequestBody.create(
            ("text/plain").toMediaTypeOrNull(),
            order_id!!
        )


        val requestBody =
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), beforeImagesList.get(0))
        val requestBody2 =
            RequestBody.create("image/jpeg".toMediaTypeOrNull(), beforeImagesList.get(1))

        val filePart1 = MultipartBody.Part.createFormData(
            "before_photo_one",
            beforeImagesList.get(0).name,
            requestBody
        )
        val filePart2 = MultipartBody.Part.createFormData(
            "before_photo_two",
            beforeImagesList.get(1).name,
            requestBody2
        )
        RetrofitClient.instance.uploadFilesBeforeCleaning(
            cleanerIdRb,
            bookingIdRb,
            notesRb,
            product_idRB,
            filePart1,
            filePart2

        )!!.enqueue(object : Callback<Orders_models?> {
            override fun onResponse(call: Call<Orders_models?>, response: Response<Orders_models?>) {
                CommonUtils.hideProgressDialog()
                if (response.isSuccessful && response.body()!!.code == 201) {
                    sharedPreferences.setBeforeImageUploaded(true, order_id!!)
                    Toast.makeText(
                        this@UploadPhotoActivity,
                        "Images Uploaded successfully.",
                        Toast.LENGTH_LONG
                    ).show()
                    setAfterCleaningView();
                    setResult(RESULT_OK)
                    finish()
                }else{
                    Toast.makeText(
                        this@UploadPhotoActivity,
                        "Something went wrong, please  try again later.",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            override fun onFailure(call: Call<Orders_models?>, t: Throwable) {
                CommonUtils.hideProgressDialog()
                Toast.makeText(
                    this@UploadPhotoActivity,
                    t.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }

        })

    }

    private fun setAfterCleaningView() {
        tv_before.isEnabled = false
        tv_before.background = resources.getDrawable(R.drawable.gray_button_bachground)
        tv_after.background = resources.getDrawable(R.drawable.before_button_bachground)
        tv_before.isEnabled = true
        iv_image_one.setImageDrawable(null)
        iv_image_two.setImageDrawable(null)
        beforeImagesList.clear()
        tv_after.isEnabled = true
        tv_before.isEnabled = false
    }

    private fun showImageOptionPopup() {
        val dialog = Dialog(this, R.style.slideFromBottomDialog)
        val window = dialog.window!!
        val wlp = window.attributes
        wlp.gravity = Gravity.BOTTOM
        wlp.flags = wlp.flags and WindowManager.LayoutParams.FLAG_DIM_BEHIND.inv()
        window.attributes = wlp
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_camera_gallery)
        val tv_dialoge_cancel = dialog.findViewById<TextView>(R.id.tv_dialoge_cancel)
        tv_dialoge_cancel.setOnClickListener { dialog.dismiss() }
//        val tv_gallery = dialog.findViewById<TextView>(R.id.tv_library)
        val tv_camera = dialog.findViewById<TextView>(R.id.camera)
        dialog.setCancelable(true)
        tv_camera.setOnClickListener {
            dialog.dismiss()
            ImagePicker.with(this)
                .crop()
                .cameraOnly() //Crop image(Optional), Check Customization for more option
                .compress(1024) //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    1080
                ) //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
//        tv_gallery.setOnClickListener {
//            dialog.dismiss()
//            ImagePicker.with(this)
//                .crop()
//                .galleryOnly() //Crop image(Optional), Check Customization for more option
//                .compress(1024) //Final image size will be less than 1 MB(Optional)
//                .maxResultSize(
//                    1080,
//                    1080
//                ) //Final image resolution will be less than 1080 x 1080(Optional)
//                .start()
//        }
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && data != null && data.data != null) {
            //Image Uri will not be null for RESULT_OK
            val uri = data.data
            beforeImagesList.add(File(uri!!.path))
            tv_photo_count.text="Uploaded photos: "+beforeImagesList.size
            if (beforeImagesList.size > 1) {
                iv_image_two.setImageURI(uri)
                enableUploadButton()
            } else iv_image_one.setImageURI(uri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun enableUploadButton() {
        btn_upload.isEnabled = true
        btn_upload.setBackgroundColor(R.color.app_color)
    }
}