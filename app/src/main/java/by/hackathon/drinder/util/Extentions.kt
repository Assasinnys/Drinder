package by.hackathon.drinder.util

import androidx.fragment.app.Fragment
import by.hackathon.drinder.App
import by.hackathon.drinder.ui.MainActivity
import by.hackathon.drinder.ui.MyProfileActivity

fun Fragment.mainActivity(): MainActivity = (requireActivity() as MainActivity)

fun Fragment.myProfileActivity(): MyProfileActivity = (requireActivity() as MyProfileActivity)

fun Fragment.myApp(): App = (requireActivity().application as App)