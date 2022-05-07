package com.applications.cleaner.utils


import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import com.applications.cleaner.ApplicationGlobal
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices


class LocationGetter(private val mContext: Context) : Service(),
    GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
    LocationListener {
    private var googleApiClient: GoogleApiClient? = null
    private var location: Location? = null
    private val fusedLocationProviderApi = LocationServices.FusedLocationApi
    private var locationRequest: LocationRequest? = null
    private val TAG = "MyAwesomeApp"
    fun stopService() {
        stopSelf()
    }

    private fun initialiseItems() {
        locationRequest = LocationRequest.create()
        locationRequest!!.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest!!.interval = INTERVAL
        locationRequest!!.fastestInterval = FASTEST_INTERVAL
        googleApiClient = GoogleApiClient.Builder(mContext)
            .addApi(LocationServices.API).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).build()
        if (googleApiClient != null) {
            try {
                googleApiClient!!.connect()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        Log.e(TAG, "GPSTrackerNew destroyed!")
        // Disconnecting the client invalidates it.
        if (googleApiClient!!.isConnected) googleApiClient!!.disconnect()
        super.onDestroy()
    }

    override fun onCreate() {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    override fun onConnected(connectionHint: Bundle?) {
        if (ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val currentLocation = fusedLocationProviderApi
            .getLastLocation(googleApiClient!!)
        if (currentLocation != null) {
            location = currentLocation
            ApplicationGlobal.gLat = location!!.latitude
            ApplicationGlobal.gLng = location!!.longitude
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
            googleApiClient!!,
            locationRequest!!
        ) { loc ->
            if (loc != null) {
                location = loc
                ApplicationGlobal.gLat = location!!.latitude
                ApplicationGlobal.gLng = location!!.longitude
            }
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    override fun onLocationChanged(location: Location) {
        if (location != null) {
            if (null == this.location
                || location.accuracy < this.location!!.accuracy
            ) {
                this.location = location
                // Toast.makeText(mContext, location.getLatitude()+"<><><>Changed<><><>"+location.getLongitude(), Toast.LENGTH_SHORT).show();
                ApplicationGlobal.gLat = location.latitude
                ApplicationGlobal.gLng = location.longitude
            }
        }
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        initialiseItems()
        return super.onStartCommand(intent, flags, startId)
    }

    companion object {
        private const val INTERVAL = (1000 * 1).toLong()
        private const val FASTEST_INTERVAL = (500 * 1).toLong()
    }


}
