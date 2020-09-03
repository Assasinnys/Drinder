package by.hackathon.drinder.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import by.hackathon.drinder.App
import by.hackathon.drinder.R
import by.hackathon.drinder.ui.MainActivity
import by.hackathon.drinder.ui.MyProfileActivity
import com.google.android.material.textfield.TextInputLayout

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

fun Fragment.setEditTextError(textInputLayout: TextInputLayout, errorCode: Int) {
    textInputLayout.apply {
        when (errorCode) {
            NO_ERROR -> {
                applyError("")
            }
            ERR_EMPTY_FIELD -> {
                applyError(getString(R.string.error_empty_field))
            }
            ERR_AGE_TOO_HIGH -> {
                applyError(getString(R.string.error_age_too_high))
            }
            ERR_AGE_ZERO -> {
                applyError(getString(R.string.error_age_positive))
            }
            ERR_PASS_NOT_EQ -> {
                applyError(getString(R.string.error_pass_not_eq))
            }
            ERR_REG -> {
                applyError(getString(R.string.error_reg))
            }
            ERR_USER_NOT_EXIST -> {
                applyError(getString(R.string.error_user_not_exist))
            }
        }
    }
}

fun TextInputLayout.applyError(errorText: String) {
    if (errorText.isNotEmpty()) {
        isErrorEnabled = true
        error = errorText
    } else {
        isErrorEnabled = false
    }
}

fun AndroidViewModel.getApp(): App = getApplication() as App