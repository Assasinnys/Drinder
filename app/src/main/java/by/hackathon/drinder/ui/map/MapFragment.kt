package by.hackathon.drinder.ui.map

import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.hackathon.drinder.R
import by.hackathon.drinder.api.ApiImplementation
import by.hackathon.drinder.util.isLocationPermissionGranted
import by.hackathon.drinder.util.myApp
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
import kotlinx.android.synthetic.main.fragment_map.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    private val navController by lazy { findNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!requireContext().isLocationPermissionGranted()) {
            Toast.makeText(
                context,
                "You didn't give permission to access your location",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_maps_activity, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.myProfile -> {
                navController.navigate(R.id.action_mapFragment_to_userDetailFragment)
                false
            }
            R.id.sendMyLocation -> {
                //TODO send location
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //preventing schools, universities and some other objects from being displayed
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.style_json))

        updateLocationUI()
        MainScope().launch { showDrinkers(getUserId()) }
        getDeviceLocation()
        if(mLastKnownLocation != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()))
    }

    private fun updateLocationUI(){
        try {
            if (requireContext().isLocationPermissionGranted()) {
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

    private fun getDeviceLocation() {
        try {
            val locationResult: Task<Location> = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener() { p0 ->
                if (p0.isSuccessful) {
                    // Set the map's camera position to the current location of the device.
                    mLastKnownLocation = p0.result
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        LatLng(
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

    private suspend fun showDrinkers(myId: String) {
        val markers = ApiImplementation.findDrinkers(myId)
        markers.forEach { marker ->
            val m = mMap.addMarker(
                MarkerOptions()
                .position(LatLng(marker.lat.toDouble(), marker.lon.toDouble()))
                .title("drinker number ${marker.id}"))
            m.tag = marker.id
        }
    }

    private fun getUserId(): String = myApp().userManager.loginInfo?.id?:"0"

    private fun getLatLng() = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)
}
