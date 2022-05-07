package com.applications.cleaner.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.TextUtils
import android.util.DisplayMetrics
import android.util.Patterns
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.applications.cleaner.R
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import com.pnikosis.materialishprogress.ProgressWheel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object CommonUtils {
    var dialog: Dialog? = null
    fun getDisplayHeightWidth(activity: Activity): Int {
        val displayMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels
        return height
    }

    fun round(value: Double, places: Int): Double {
        var value = value
        require(places >= 0)
        val factor = Math.pow(10.0, places.toDouble()).toLong()
        value = value * factor
        val tmp = Math.round(value)
        return tmp.toDouble() / factor
    }


    fun checkNetworkAvailability(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        @SuppressLint("MissingPermission") val info = cm.activeNetworkInfo
        if (info == null) {
            Toast.makeText(context, "Internet not connected.", Toast.LENGTH_LONG).show()
            return false
        }
        val network = info.state
        return if (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING) true else {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG)
                .show()
            false
        }
    }

    fun hideKeyboard(context: Activity) {
        val view = context.currentFocus
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun IsValidEmail(email: String?): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    fun showToast(applicationContext: Context?, s: String?) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("SimpleDateFormat")
    fun getFormattedDate(time: String?, format: String?, new_format: String?): String {
        val sdf = SimpleDateFormat(format)
        var date1: Date? = null
        var timeSend = ""
        try {
            date1 = sdf.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        try {
            timeSend = SimpleDateFormat(new_format).format(date1)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return timeSend
    }


/*
    fun loadProfileImage(url: String?, iv_profile: ImageView) {
        try {
            if (url != null && !url.isEmpty()) {
                if (url.startsWith("http")) Picasso.get()
                    .load(url)
                    .resize(300, 300)
                    .centerCrop()
                    .error(R.drawable.user_placeholder)
                    .into(iv_profile) else Picasso.get()
                    .load(Constant.BASE_URl_IMAGE.toString() + url)
                    .resize(300, 300)
                    .error(R.drawable.user_placeholder)
                    .centerCrop()
                    .into(iv_profile)
            } else {
                iv_profile.setImageResource(R.drawable.user_placeholder)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/


    fun getAddress(ctx: Context?, lat: String, lng: String): String {
        val geocoder = Geocoder(ctx, Locale.getDefault())
        return try {
            val addresses =
                geocoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)
            val obj = addresses[0]
            /*            add = add + ", " + obj.getCountryName();
                   add = add + ", " + obj.getCountryCode();
                   add = add + ", " + obj.getAdminArea();
                   add = add + ", " + obj.getPostalCode();*/obj.getAddressLine(0)
        } catch (e: Exception) {
            // TODO Auto-generated catch block
            ""
        }
    }

    fun isAtDestination(
        lat: String?,
        lng: String?,
        gLat: Double?,
        gLng: Double?,
        requireContext: Context
    ): Double {

        val sLat = lat?.toDouble()
        val sLng = lng?.toDouble()
        val dLat = gLat?.toDouble()
        val dLng = gLng?.toDouble()
        val distance: Double =
            SphericalUtil.computeDistanceBetween(LatLng(sLat!!, sLng!!), LatLng(dLat!!, dLng!!))
        return distance;
    }

    fun initProgressDialog(context: Context) {
        try {
            hideProgressDialog()
            dialog = Dialog(context)
            dialog!!.setCancelable(false)
            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog!!.setContentView(R.layout.inflate_progress_bar)
            dialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val display = (context as Activity).windowManager.defaultDisplay
            val wheel: ProgressWheel = dialog!!.findViewById(R.id.progreeWheel)
            wheel.setBarColor(context.getResources().getColor(R.color.purple_500))
            wheel.setRimWidth(wheel.getBarWidth() * 3)
            val size = Point()
            display.getSize(size)
            val width = size.x
            dialog!!.window!!.setLayout(
                width - 60,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ) // set width of alert dialog box nad get width dynamically
            dialog!!.show()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun hideProgressDialog() {
        try {
            if (dialog != null) {
                dialog!!.dismiss()
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}