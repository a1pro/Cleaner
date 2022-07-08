package com.applications.cleaner.Fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.applications.cleaner.ApplicationGlobal
import com.applications.cleaner.R
import com.applications.cleaner.utils.FetchURL
import com.applications.cleaner.utils.LocationGetter
import com.applications.cleaner.utils.TaskLoadedCallback
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions


class DirectionFragment : Fragment(), TaskLoadedCallback {


    private lateinit var mMap: GoogleMap
    private var currentPolyline: Polyline? = null

    private lateinit var googleMap: GoogleMap
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2
    private var lat: String? = ""
    private var lng: String? = ""
    private var progressBar: ProgressBar? = null

    @SuppressLint("MissingPermission")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_getdirection_, container, false)
        progressBar = view.findViewById(R.id.progress_Bar) as ProgressBar

        if (arguments != null) {
            // The getPrivacyPolicyLink() method will be created automatically.
            lat = requireArguments().getString("lat", "")
            lng = requireArguments().getString("lng", "")
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getLocation()



        return view
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            Log.e("Location", ":11111111 ")
            if (isLocationEnabled()) {
                progressBar!!.visibility = View.VISIBLE
                val locationGetter = LocationGetter(requireContext())

                callGetLocation();
                Log.e("Location", "getLocation:11111111 ")
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    Log.e("Location", "getLocation: " + location)
                    if (location != null) {
                        ApplicationGlobal.gLat = location.latitude
                        ApplicationGlobal.gLng = location.longitude
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

    private fun callGetLocation() {
        Handler().postDelayed({
            if (ApplicationGlobal.gLat < 0 || ApplicationGlobal.gLat > 0 || ApplicationGlobal.gLng < 0 ||
                ApplicationGlobal.gLng > 0
            ) {
                try {
                    loadMapAndRoute()
                } catch (e: Exception) {
                }
            } else
                callGetLocation()

        }, 1000)


    }

    @RequiresApi(Build.VERSION_CODES.CUPCAKE)
    private fun loadMapAndRoute() {
        progressBar!!.visibility = View.GONE

        val mapsupport =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapsupport.getMapAsync(OnMapReadyCallback { it ->
            googleMap = it


            val location1 = LatLng(ApplicationGlobal.gLat, ApplicationGlobal.gLng)
            Log.e("Location", "getLocation: " + location1)
            googleMap.addMarker(
                MarkerOptions().position(location1).title("My Location")
            )
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    location1,
                    15F
                )
            )
            this.mMap = googleMap;
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return@OnMapReadyCallback
            }
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isZoomControlsEnabled = true
            val source = LatLng(5.691856, -4.775021)
            val destination = LatLng(0.252472, 25.461649)
            Handler().postDelayed({
                FetchURL(this).execute(
                    "https://maps.googleapis.com/maps/api/directions/json?origin=" + ApplicationGlobal.gLat + "," +
                            ApplicationGlobal.gLng + "&destination=" + lat + "," + lng + "&sensor=false&key=AIzaSyCouZmiYT2D8YJntl-0Kd1Gmr18OQt65TI",
                    "driving"
                )
            }, 1000)


        })

        /*   FetchURL(this).execute(
               "https://maps.googleapis.com/maps/api/directions/json?origin="+Latitude+","+Longitude+"&destination="+lat+","+lng+"&sensor=false&key=AIzaSyCouZmiYT2D8YJntl-0Kd1Gmr18OQt65TI",
               "driving"
           )*/

    }


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
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

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    override fun onTaskDone(vararg values: Any?) {
        if (currentPolyline != null) currentPolyline!!.remove()
        currentPolyline = (values[0] as PolylineOptions?)?.let { mMap.addPolyline(it) }
    }

}