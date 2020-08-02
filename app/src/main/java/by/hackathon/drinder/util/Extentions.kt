package by.hackathon.drinder.util

import android.app.Application
import androidx.fragment.app.Fragment
import by.hackathon.drinder.App
import by.hackathon.drinder.ui.MainActivity

fun Fragment.mainActivity(): MainActivity = (requireActivity() as MainActivity)

fun Fragment.myApp(): App = (requireActivity().application as App)