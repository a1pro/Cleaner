package com.applications.cleaner.Fragments


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applications.cleaner.*
import com.applications.cleaner.Models.Order_Data
import com.applications.cleaner.Models.Orders_models
import com.applications.cleaner.R
import com.applications.cleaner.Retrofit.RetrofitClient
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.applications.cleaner.utils.CommonUtils
import com.applications.cleaner.utils.LocationGetter
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OrderDetailFragment : Fragment() {
    private var order_id: String? = ""
    private var singleOven: String? = ""
    private var totalAmout: String? = ""
    private var activity: Activity? = null
    private lateinit var sharedPreferences: My_Sharepreferences
    private var lat: String? = ""
    private var lng: String? = ""
    private lateinit var btn_complete_order: Button
    private lateinit var get_direction: Button
    private lateinit var upload_photo: Button
    private lateinit var arrived_at_location: Button

    private lateinit var conferm_order: Button
    private lateinit var btn_verift_otp: Button
    private lateinit var btn_generate_otp: Button

    private lateinit var btn_start_cleaning: Button
    private lateinit var btn_complete_cleaning: Button
    private lateinit var btn_completed: Button
    private lateinit var problem_with_order: Button
    private lateinit var orderstatus_text: TextView
    private lateinit var text: String
    private lateinit var cleaning_time: TextView
    private lateinit var name_order: TextView
    private lateinit var phone_no: TextView
    private lateinit var rl_get_loc: RelativeLayout
    private lateinit var address: TextView
    private lateinit var cleantype: TextView
    private lateinit var addons: TextView
    private lateinit var total_amount: TextView
    private lateinit var collect_amount: TextView
    private lateinit var order_no: TextView
    private lateinit var instructions: TextView
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var data: Order_Data


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_order__detail_, container, false)
        sharedPreferences = My_Sharepreferences(requireContext())
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        activity = getActivity();
        orderstatus_text = view.findViewById<TextView>(R.id.orderstatus_text)
        btn_start_cleaning = view.findViewById<Button>(R.id.btn_start_cleaning)
        btn_completed = view.findViewById<Button>(R.id.btn_completed)
        btn_complete_cleaning = view.findViewById<Button>(R.id.btn_complete_cleaning)
        btn_complete_order = view.findViewById<Button>(R.id.btn_complete_order)
        get_direction = view.findViewById<Button>(R.id.get_direction)
        arrived_at_location = view.findViewById<Button>(R.id.arrived_at_location)
        conferm_order = view.findViewById<Button>(R.id.conferm_order)
        problem_with_order = view.findViewById<Button>(R.id.problem_with_order)
        btn_verift_otp = view.findViewById(R.id.btn_verift_otp)
        btn_generate_otp = view.findViewById(R.id.btn_generate_otp)
        upload_photo = view.findViewById(R.id.upload_photo)
        order_no = view.findViewById(R.id.order_no)
        cleaning_time = view.findViewById(R.id.cleaning_time)
        name_order = view.findViewById(R.id.name_order)
        phone_no = view.findViewById(R.id.phone_no)
        rl_get_loc = view.findViewById(R.id.rl_get_loc)
        address = view.findViewById(R.id.address)
        cleantype = view.findViewById(R.id.cleantype)
        addons = view.findViewById(R.id.addons)
        total_amount = view.findViewById(R.id.total_amount)
        collect_amount = view.findViewById(R.id.collect_amount)
        order_no = view.findViewById(R.id.order_no)
        instructions = view.findViewById(R.id.instructions)

        get_direction.visibility = View.VISIBLE
        arrived_at_location.visibility = View.VISIBLE
        conferm_order.visibility = View.GONE
        problem_with_order.visibility = View.GONE
        upload_photo.visibility = View.GONE

        data = requireArguments().getSerializable("data") as Order_Data
        lat = requireArguments().getString("lat")
        lng = requireArguments().getString("lng")
        order_id = requireArguments().getString("bookingId")
        val status = requireArguments().getString("status")
        val preferredTime = requireArguments().getString("preferredTime")
        val name = requireArguments().getString("name")
        val phone = requireArguments().getString("phone")
        val address_1 = requireArguments().getString("address")
        singleOven = requireArguments().getString("singleOven")
        val extraProduct = requireArguments().getString("extraProduct")
        totalAmout = requireArguments().getString("totalAmout")
        val otherNotes = requireArguments().getString("otherNotes")

        order_no.setText(order_id)
        if (status.equals("1")) {
            orderstatus_text.setText("To Be Confirmed")
        } else if (status.equals("2")) {
            orderstatus_text.setText("Confirmed")
        } else if (status.equals("3")) {
            orderstatus_text.setText("Rebooked")
        } else if (status.equals("4")) {
            orderstatus_text.setText("Completed")
        } else if (status.equals("5")) {
            orderstatus_text.setText("Cancelled")
        } else if (status.equals("6")) {
            orderstatus_text.setText("Rescheduled")
        } else {
            orderstatus_text.setText("")
        }

        cleaning_time.setText(preferredTime)
        name_order.setText(name)
        phone_no.setText(phone)
        address.setText(address_1)
        cleantype.setText(singleOven)
        addons.setText(extraProduct)
        total_amount.setText(totalAmout)
        collect_amount.setText(totalAmout)
        if (data != null && data.paidAmount != null && !data.paidAmount.toString().isEmpty())
            collect_amount.setText(
                (totalAmout!!.toDouble() - data.paidAmount.toString().toDouble()).toString() + ""
            )

        /*  Intent i = new Intent(this, ChatProductDetailActivity.class);
            i.putExtra("data", chatMessagesList.get(pos).getProductData());
            i.putExtra("pos", pos);
            chatProductDetailsResultLauncher.launch(i);*/

        /*  Intent i = new Intent(this, ChatProductDetailActivity.class);
            i.putExtra("data", chatMessagesList.get(pos).getProductData());
            i.putExtra("pos", pos);
            chatProductDetailsResultLauncher.launch(i);*/

        instructions.setText(otherNotes)




        try {
            text = requireArguments().getString("value").toString()
            Log.e("value", "on create " + text)

            if (text?.equals("1") == true) {
                get_direction.visibility = View.GONE
                arrived_at_location.visibility = View.GONE
                conferm_order.visibility = View.GONE
                problem_with_order.visibility = View.GONE
                upload_photo.visibility = View.VISIBLE
                orderstatus_text.setText("Cleaning")
            }
        } catch (e: Exception) {

        }
        get_direction.setOnClickListener { view ->
            val bundle = Bundle()
            bundle.putString("lng", lng)
            bundle.putString("lat", lat) // Serializable Object

            mFusedLocationClient.lastLocation.addOnCompleteListener {task ->
                val location : Location? = task.result
                if (location != null){
                    Log.e("location", "1234567: ")
                    findNavController().navigate(R.id.action_order_Detail_Fragment_to_getdirection_Fragment, bundle)
                }
                else{
                    location_dialog()
                }
            }



        }


        btn_complete_order.setOnClickListener { view ->
            callCompleteOrderAPI()
            //ArrivedorderDailog(context)
        }
        arrived_at_location.setOnClickListener { view ->
            getLocation(true)
            //ArrivedorderDailog(context)
        }
        problem_with_order.setOnClickListener { view ->
            showAddProblemNotesDialog();
        }
        conferm_order.setOnClickListener { view ->

            //ConferorderDailog(context)
            callConfirmOrderAPI()
        }

        btn_complete_cleaning.setOnClickListener { view ->

            val intent = Intent(
                Intent(activity, UploadPhotoActivity::class.java)
                    .putExtra("product_id", data.singleOven+"")
                    .putExtra("order_id", order_id)
                    .putExtra("cleaner_id", sharedPreferences.getlogin())
            )
            uploadActivityResult.launch(intent)
        }
        btn_start_cleaning.setOnClickListener { view ->

            callStartCleaning()
        }

        btn_verift_otp.setOnClickListener { view ->

            showVerifyPopup()
        }
        btn_generate_otp.setOnClickListener { view ->

            callGenerateOtp()
        }
        getLocation(false)
        if (sharedPreferences.isBeforeImageUploaded(order_id!!)) {
            setStartCleaningView()
        }
        return view
    }

    private fun location_dialog() {

        val mLocationRequest: LocationRequest = LocationRequest.create()
        mLocationRequest.setInterval(60000)
        mLocationRequest.setFastestInterval(5000)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        if (location != null){
                            try {


                                val   currentlatitude = location.latitude
                                val  currentlongitude =location.longitude

                                Log.e("location", "tasklocation: $currentlatitude , $currentlongitude")

                                getLocation(false)
                            }catch (e:Exception){
                                Log.e("location", "tasklocation: $e")

                            }

//                            googleMap.uiSettings.isCompassEnabled = true
//                            googleMap.uiSettings.isZoomControlsEnabled = true

                        }
                        else{
                            Log.e("location", "tasklocation ")

                        }

                    }
                    else{
                        dailognulllocation()
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(context!!)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)






    }

    private fun dailognulllocation() {


        val dialog = requireContext()?.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.location_dailog)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val tv_submit = dialog?.findViewById<TextView>(R.id.tv_submit)

        tv_submit?.setOnClickListener { view ->

            dialog?.dismiss()

        }

        dialog?.show()
    }

    private fun callCompleteOrderAPI() {


        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.completeOrder(
            sharedPreferences.getlogin()!!,
            order_id.toString()
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
                        activity!!.onBackPressed()
                    }

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })

    }

    private fun showVerifyPopup() {

        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dailog_verify_otp)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val tv_submit = dialog?.findViewById<TextView>(R.id.tv_submit)
        val et_otp = dialog?.findViewById<EditText>(R.id.et_otp)


        tv_submit?.setOnClickListener { view ->
            if (!et_otp!!.text.toString().isEmpty()) {
                dialog!!.dismiss()
                callVerifyOtp(et_otp!!.text.toString())
            }

        }
        dialog?.show()
    }

    private fun callVerifyOtp(otp: String) {

        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.verifyOtp(
            sharedPreferences.getlogin()!!,
            order_id.toString(),
            otp.toString()
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
                        val intent = Intent(
                            Intent(activity, PaymentMethodActivity::class.java)
                                .putExtra("data", data)
                                .putExtra("order_id", order_id)
                                .putExtra("totalAmout", totalAmout)
                                .putExtra("singleOven", singleOven)
                                .putExtra("cleaner_id", sharedPreferences.getlogin())
                        )
                        paymentMethodActivityResult.launch(intent)
                    }

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })

    }

    private fun callGenerateOtp() {


        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.generateOTP(
            sharedPreferences.getlogin()!!,
            order_id.toString()
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()

                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    private fun callStartCleaning() {


        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.startClean(
            sharedPreferences.getlogin()!!,
            order_id.toString()
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    /* if (response.isSuccessful) {
                         btn_start_cleaning.visibility = View.GONE
                         btn_complete_cleaning.visibility = View.VISIBLE
                     }*/
                    btn_start_cleaning.visibility = View.GONE
                    btn_complete_cleaning.visibility = View.VISIBLE
                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()
                    btn_start_cleaning.visibility = View.GONE
                    btn_complete_cleaning.visibility = View.VISIBLE
                }

            })
    }

    private fun setStartCleaningView() {

        btn_complete_cleaning.visibility = View.GONE
        btn_start_cleaning.visibility = View.VISIBLE
        conferm_order.visibility = View.GONE
        problem_with_order.visibility = View.GONE
        get_direction.visibility = View.GONE
        arrived_at_location.visibility = View.GONE
    }

    private fun showAddProblemNotesDialog() {
        val dialog = requireContext()?.let { Dialog(it) }
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
        CommonUtils.initProgressDialog(requireContext())
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
                            requireContext(),
                            "Your problem has been registered successfully.",
                            Toast.LENGTH_SHORT
                        ).show()
                        activity!!.onBackPressed()

                    }
                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    private fun callConfirmOrderAPI() {
        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.confirmOrder(sharedPreferences.getlogin()!!, order_id!!)
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {
                        //showGenderPopup();
                        val intent = Intent(
                            Intent(activity, AddOnsActivity::class.java)
                                .putExtra("order_id", order_id)
                                .putExtra("product_id", data.singleOven+"")
                                .putExtra(
                                    "cleaner_id", sharedPreferences.getlogin()
                                )
                        )
                        uploadActivityResult.launch(intent)
                        /*  startActivity(Intent(activity, AddOnsActivity::class.java)
                              .putExtra("order_id",order_id)
                              .putExtra("cleaner_id",sharedPreferences.getlogin())
                          )  */ //callDonatesFragment();
                        //  startActivity(Intent(requireContext(), AddOnsActivity::class.java))


                    }
                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    var uploadActivityResult = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result != null && result.data != null && result.data!!.hasExtra("feedbackAdded")) {
                showPaymentView()

                return@registerForActivityResult

            }
            if (result.data != null && result.data!!.hasExtra("refreshAPI")) {
                btn_completed.visibility = View.VISIBLE
                btn_complete_cleaning.visibility = View.GONE
                btn_start_cleaning.visibility = View.GONE
                conferm_order.visibility = View.GONE

                problem_with_order.visibility = View.GONE
                requireActivity().onBackPressed()
            }
            btn_complete_cleaning.visibility = View.GONE
            btn_start_cleaning.visibility = View.VISIBLE
            conferm_order.visibility = View.GONE
            problem_with_order.visibility = View.GONE
        }
    }

    var paymentMethodActivityResult = registerForActivityResult(
        StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (result != null && result.data != null && result.data!!.hasExtra("completeOrder")) {
                btn_completed.visibility = View.VISIBLE
                btn_complete_cleaning.visibility = View.GONE
                btn_start_cleaning.visibility = View.GONE
                conferm_order.visibility = View.GONE
                problem_with_order.visibility = View.GONE
                problem_with_order.visibility = View.GONE
                btn_generate_otp.visibility = View.GONE
                btn_verift_otp.visibility = View.GONE
                get_direction.visibility = View.GONE
                btn_complete_order.visibility = View.VISIBLE
                btn_completed.visibility = View.GONE
                return@registerForActivityResult
            }
            if (result != null && result.data != null && result.data!!.hasExtra("feedbackAdded")) {
                showPaymentView()

                return@registerForActivityResult

            }
            if (result.data != null && result.data!!.hasExtra("refreshAPI")) {
                btn_completed.visibility = View.VISIBLE
                btn_complete_cleaning.visibility = View.GONE
                btn_start_cleaning.visibility = View.GONE
                conferm_order.visibility = View.GONE
                problem_with_order.visibility = View.GONE
                requireActivity().onBackPressed()
            }
            btn_complete_cleaning.visibility = View.GONE
            btn_start_cleaning.visibility = View.VISIBLE
            conferm_order.visibility = View.GONE
            problem_with_order.visibility = View.GONE
        }
    }

    private fun showPaymentView() {
        btn_completed.visibility = View.GONE
        btn_complete_cleaning.visibility = View.GONE
        btn_start_cleaning.visibility = View.GONE
        conferm_order.visibility = View.GONE
        problem_with_order.visibility = View.GONE
        btn_verift_otp.visibility = View.VISIBLE
        btn_generate_otp.visibility = View.VISIBLE

    }

    private fun ConferorderDailog(context: Context?) {
        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dailog_arrived_location)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val yes = dialog?.findViewById<TextView>(R.id.yes)
        val no = dialog?.findViewById<TextView>(R.id.no)
        val textdialog_conferm = dialog?.findViewById<TextView>(R.id.textdialog_conferm)

        yes?.visibility = View.GONE
        textdialog_conferm?.setText("Please ask customer,if they would like to clean any of the following appliances?")
        no?.setText("ok")

        no?.setOnClickListener { view ->
            dialog?.dismiss()

//            val add=Addons_Fragment()
//            fragmentManager?.beginTransaction()?.add(R.id.framed,add,null)?.commit()

            findNavController().navigate(R.id.action_order_Detail_Fragment_to_addons_Fragment)

        }
        dialog?.show()
    }


    private fun ArrivedorderDailog(context: Context?) {

        val dialog = context?.let { Dialog(it) }
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.dailog_arrived_location)
        dialog?.setCanceledOnTouchOutside(true);
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        val yes = dialog?.findViewById<TextView>(R.id.yes)
        val no = dialog?.findViewById<TextView>(R.id.no)

        yes?.setOnClickListener { view ->

            get_direction.visibility = View.GONE
            arrived_at_location.visibility = View.GONE
            conferm_order.visibility = View.VISIBLE
            problem_with_order.visibility = View.VISIBLE
            upload_photo.visibility = View.GONE
            orderstatus_text.setText("Arrived At Location")
            dialog?.dismiss()

        }
        no?.setOnClickListener { view ->
            dialog?.dismiss()
        }
        dialog?.show()
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation(click: Boolean) {
        if (checkPermissions()) {
            Log.e("Location", ":11111111 ")
            if (isLocationEnabled()) {
//                rl_get_loc!!.visibility = View.VISIBLE
                val locationGetter = LocationGetter(requireContext())

                callGetLocation(click);
                mFusedLocationClient.lastLocation.addOnCompleteListener {task ->
                    val location : Location? = task.result
                    if (location != null){
                        ApplicationGlobal.gLat = location.latitude
                        ApplicationGlobal.gLng = location.longitude
                        //callUpdateLocationAPI()

                    }
                    else{
                        location_dialog()
                    }

                }
            } else {
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun callUpdateLocationAPI() {

        Log.e("findlocatin", "callGetLocation: ${ApplicationGlobal.gLat} , ${ApplicationGlobal.gLng},")

        RetrofitClient.instance.updateLocation(
            sharedPreferences.getlogin()!!,
            ApplicationGlobal.gLat.toString() + "",
            ApplicationGlobal.gLng.toString() + ""
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {

                    }
                }

                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })

    }

    private fun callGetLocation(click: Boolean) {

        Handler().postDelayed({
            if (ApplicationGlobal.gLat < 0 || ApplicationGlobal.gLat > 0 || ApplicationGlobal.gLng < 0 ||
                ApplicationGlobal.gLng > 0
            ) {
                callUpdateLocationAPI()
//                rl_get_loc!!.visibility = View.GONE
                val distance = CommonUtils.isAtDestination(
                    lat,
                    lng,
                    ApplicationGlobal.gLat,
                    ApplicationGlobal.gLng,
                    requireContext()
                ) as Double

                if (distance != null) {
                    if (click)
                        callArrivedAtLocationAPI();
                } else {
                    if (click)
                    // callArrivedAtLocationAPI();
                        Toast.makeText(
                            requireContext(),
                            "You are not at the destination",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            } else
                callGetLocation(click)

        }, 1000)


    }

    private fun callArrivedAtLocationAPI() {


        CommonUtils.initProgressDialog(requireContext())
        RetrofitClient.instance.arriverAtLocation(
            sharedPreferences.getlogin()!!,
            order_id.toString()
        )
            .enqueue(object : Callback<Orders_models> {

                override fun onResponse(
                    call: Call<Orders_models>,
                    response: Response<Orders_models>
                ) {
                    CommonUtils.hideProgressDialog()
                    if (response.isSuccessful) {
                        get_direction.visibility = View.GONE
                        arrived_at_location.visibility = View.GONE
                        conferm_order.visibility = View.VISIBLE
                        problem_with_order.visibility = View.VISIBLE
                        if (sharedPreferences.isBeforeImageUploaded(order_id!!)) {
                            setStartCleaningView()
                        }
                    }
                }


                override fun onFailure(call: Call<Orders_models>, t: Throwable) {
                    CommonUtils.hideProgressDialog()

                }

            })
    }

    private fun getRemainingDistance() {
        val str_origin = "origin=" + lat + "," + lng

        // Destination of route
        val str_dest = "destination=" + ApplicationGlobal.gLat + "," + ApplicationGlobal.gLng


        // Sensor enabled
        val sensor = "sensor=false"
        val mode = "mode = driving"
        val parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
        val url = "https://maps.googleapis.com/maps/api/directions/json" + "?" + parameters
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    private val permissionId = 2

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation(false)
            }
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

}
