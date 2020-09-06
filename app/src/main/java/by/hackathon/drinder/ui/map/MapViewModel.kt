package by.hackathon.drinder.ui.map

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.*
import by.hackathon.drinder.R
import by.hackathon.drinder.data.LocationInfo
import by.hackathon.drinder.data.repository.MapRepository
import by.hackathon.drinder.util.DEFAULT_USER
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
class MapViewModel @Inject constructor(
    val repository: MapRepository,
    val appContext: Context
) : ViewModel(), DefaultLifecycleObserver,
    OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private var mLastKnownLocation: Location? = null

    private val isGpsPermissionGranted = MutableLiveData(false)
    private val _navigateToUserDetailPermission = MutableLiveData(DEFAULT_USER)

    val gpsPermissionState: LiveData<Boolean> get() = isGpsPermissionGranted
    val navigateToUserDetail: LiveData<String> get() = _navigateToUserDetailPermission

    override fun onCreate(owner: LifecycleOwner) {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(appContext)
        if (appContext.isLocationPermissionGranted()) {
            isGpsPermissionGranted.value = true
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        _navigateToUserDetailPermission.value = DEFAULT_USER
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val mapStyle =
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
                R.raw.style_json_n
            else
                R.raw.style_json
        googleMap.setMapStyle(
            MapStyleOptions.loadRawResourceStyle(
                appContext,
                mapStyle
            )
        )

        updateLocationUI()
        viewModelScope.launch {

            val markers = repository.findDrinkers()
            showDrinkers(markers, repository.getOwnId())
        }
        getDeviceLocation()
        if (mLastKnownLocation != null) mMap.moveCamera(CameraUpdateFactory.newLatLng(getLatLng()))
    }

    private fun showDrinkers(markers: List<LocationInfo>, myId: String?) {
        markers.forEach { marker ->
            if (marker.id != myId) {
                val m = mMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(marker.lat.toDouble(), marker.lon.toDouble()))
                        .title("${appContext.getString(R.string.marker_drinker)} ${marker.id}")
                )
                m.tag = marker.id
            }
        }
        mMap.setOnMarkerClickListener {
            _navigateToUserDetailPermission.value = it.tag as String
            true
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
            viewModelScope.launch {
                repository.sendLocation(location.latitude, location.longitude)
            }
        }
    }
}
