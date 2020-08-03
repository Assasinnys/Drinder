package by.hackathon.drinder

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.ui.MyProfileActivity
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_maps_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.myProfile -> {
                val intent = Intent(this, MyProfileActivity::class.java)
                startActivity(intent)
                false
            }
            R.id.sendMyLocation -> {
                sendLocation(getId(), getLatLng())
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
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
        MainScope().launch { showDrinkers(getId()) }
        getDeviceLocation()
        if(mLastKnownLocation != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()))
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

    private fun getId(): String = (application as App).userManager.loginInfo?.id?:"0"

    private fun getLatLng() = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)

    private fun sendLocation(id: String, location: LatLng) {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                ApiImplementation.sendLocation(id, location.latitude, location.longitude)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            // refreshes markers. Uncomment if find way to delete existing markers
            // MainScope().launch { showDrinkers(getId()) }
        }
    }

    companion object {
        private const val REQUEST_CODE = 12
    }
}