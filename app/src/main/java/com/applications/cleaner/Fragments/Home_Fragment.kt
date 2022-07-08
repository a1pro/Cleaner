package com.applications.cleaner.Fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.applications.cleaner.R
import com.applications.cleaner.Shareprefrence.My_Sharepreferences
import com.google.android.gms.location.*


class Home_Fragment : Fragment() {
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home_, container, false)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())


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
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(context!!)
            .requestLocationUpdates(mLocationRequest, mLocationCallback, null)



        val sharedPreferences =  My_Sharepreferences(requireContext())
        val orders =view.findViewById<LinearLayout>(R.id.orders);
        val Name =view.findViewById<TextView>(R.id.Name);

        Name.setText("Hi "+sharedPreferences.getusername() )




        getlocation()
        orders.setOnClickListener {

//            val ordersFragment=Orders_Fragment()
//            val transaction = activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.frame_home, ordersFragment,null)?.commit()

//            findNavController().navigate(R.id.action_home_Fragment_to_getdirection_Fragment)

                findNavController().navigate(R.id.action_home_Fragment_to_orders_Fragment)

        }


        return view ;

    }

    override fun onResume() {
        super.onResume()
        getlocation()
    }

    private fun getlocation() {
        if(checkPermissions()){
            if (loctionenable()){
                Log.e("location", "getlocation:11111111111 ")
                mFusedLocationClient.lastLocation.addOnCompleteListener {task ->
                    val location : Location? = task.result
                    if (location != null){
                        try {


                     val   currentlatitude = location.latitude
                      val  currentlongitude =location.longitude

                        Log.e("location", "1234567: $currentlatitude , $currentlongitude")

                        }catch (e:Exception){
                            Log.e("location", "1234567: $e")

                        }

//                            googleMap.uiSettings.isCompassEnabled = true
//                            googleMap.uiSettings.isZoomControlsEnabled = true
                        
                    }
                    else{
                        Log.e("location", "22222222222222 ")

                    }
                }
            }
            else{
                Toast.makeText(requireContext(), "Please turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }


        }else{
            requestpermission()
        }

    }

    private fun requestpermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION),permissionId)

    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode==permissionId){
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
            {
                getlocation()
            }
        }
    }
    private fun loctionenable(): Boolean {
        val locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER)
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(requireContext(),android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED)
        {
            return true
        }

        return false
    }


}