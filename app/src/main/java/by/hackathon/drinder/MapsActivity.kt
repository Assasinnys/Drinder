package by.hackathon.drinder

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import by.hackathon.drinder.api.ApiImplementation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        while(!isPermissionGranted()) requestPermsision()

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //preventing schools, universities and some other objects from being displayed
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json))

        updateLocationUI()
        MainScope().launch { showDrinkers(DRINKER_ID) } //TODO: get real id
        getDeviceLocation()
        if(mLastKnownLocation != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(
            LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)))
    }

    private fun isPermissionGranted(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED

    private fun requestPermsision() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
    }

    /**
     * Hide/show button for getting location
     */
    private fun updateLocationUI(){
        try {
            if (isPermissionGranted()) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    /**
     *  Get the best and most recent location of the device, which may be null in rare
     *  cases when a location is not available.
     */
    private fun getDeviceLocation() {
        try {
            val locationResult: Task<Location> = mFusedLocationProviderClient.getLastLocation()
            locationResult.addOnCompleteListener(this) { p0 ->
                if (p0.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    mLastKnownLocation = p0.result
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(
                        mLastKnownLocation!!.latitude,
                        mLastKnownLocation!!.longitude
                    ), 14F))
                } else {
                    Log.d("kj", "Current location is null. Using defaults.")
                    Log.e("kj", "Exception: %s", p0.exception)
                    mMap.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message!!)
        }
    }

    /**
     * get list of drinkers from server and shows their location on map
     */
    private suspend fun showDrinkers(myId: String) {
        val markers = ApiImplementation.findDrinkers(myId)
        markers.forEach { marker ->
            mMap.addMarker(MarkerOptions()
                .position(LatLng(marker.lat.toDouble(), marker.lon.toDouble()))
                .title("drinker number ${marker.id}"))
        }
    }

    companion object {
        private const val DRINKER_ID = "112"
        private const val REQUEST_CODE = 12
    }
}