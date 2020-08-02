package by.hackathon.drinder.util

import androidx.fragment.app.Fragment
import by.hackathon.drinder.ui.MainActivity

fun Fragment.mainActivity(): MainActivity = (requireActivity() as MainActivity)