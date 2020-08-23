package by.hackathon.drinder.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import by.hackathon.drinder.App
import by.hackathon.drinder.MapsActivity
import by.hackathon.drinder.ui.MainActivity
import by.hackathon.drinder.ui.MyProfileActivity

fun Fragment.mainActivity(): MainActivity = (requireActivity() as MainActivity)

fun Fragment.myProfileActivity(): MyProfileActivity = (requireActivity() as MyProfileActivity)

fun Fragment.myApp(): App = (requireActivity().application as App)

fun Context.isLocationPermissionGranted(): Boolean =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.requestLocationPermission() {
    ActivityCompat.requestPermissions(
        this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 12
    )
}