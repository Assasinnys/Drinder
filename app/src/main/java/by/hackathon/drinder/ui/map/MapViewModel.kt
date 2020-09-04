package by.hackathon.drinder.ui.map

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.*
import by.hackathon.drinder.R
import by.hackathon.drinder.UserManager
import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.repository.MapRepository
import by.hackathon.drinder.util.isLocationPermissionGranted
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("MemberVisibilityCanBePrivate")
class MapViewModel @Inject constructor(val userManager: UserManager, val repository: MapRepository, val appContext: Context) : ViewModel(), DefaultLifecycleObserver,
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null
    private val isGpsPermissionGranted = MutableLiveData(false)

    val gpsPermissionState: LiveData<Boolean> get() = isGpsPermissionGranted

    // DI
//    private val userManager by lazy { getApp().userManager }
//    private val repository: MapRepository by lazy { getApp().repository }

    override fun onCreate(owner: LifecycleOwner) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
        if (appContext.isLocationPermissionGranted()) {
            isGpsPermissionGranted.value = true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                appContext,
                R.raw.style_json
            )
        )

        updateLocationUI()
        viewModelScope.launch {
            userManager.loginInfo?.let {
                val markers = repository.findDrinkers(it.id)
                showDrinkers(markers, it.id)
            }
        }
        getDeviceLocation()
        if (mLastKnownLocation != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()))
    }

    private fun showDrinkers(markers: List<LocationInfo>, myId: String) {
        markers.forEach { marker ->
            if (marker.id != myId) {
                val m = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(marker.lat.toDouble(), marker.lon.toDouble()))
                        .title("drinker number ${marker.id}")
                )
                m.tag = marker.id
            }
        }
    }

    private fun getDeviceLocation() {
        try {
            val locationResult: Task<Location> = mFusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { p0 ->
                if (p0.isSuccessful) {
                    mLastKnownLocation = p0.result?.also {
                        mMap.moveCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                ), 14F
                            )
                        )
                    }
                } else {
                    Log.d("kj", "Current location is null. Using defaults.")
                    Log.e("kj", "Exception: %s", p0.exception)
                    mMap.uiSettings.isMyLocationButtonEnabled = false
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message ?: "")
        }
    }

    private fun updateLocationUI() {
        try {
            if (isGpsPermissionGranted.value!!) {
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

    private fun getLatLng() = LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude)

    fun notifyLocationSendRequest() {
        mLastKnownLocation?.let { location ->
            userManager.loginInfo?.let {
                viewModelScope.launch {
                    val isSuccessful =
                        repository.sendLocation(it.id, location.latitude, location.longitude)
                    Log.d("ASD", "$isSuccessful")
                }
            }
        }
    }
}