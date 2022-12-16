package com.udacity.project4.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

object Constants {
    const val GEOFENCE_EVENT = "GEOFENCE EVENT"
    const val GEOFENCE_RADIUS = 15f
    const val REQUEST_DEVICE_LOCATION = 1808
    const val REQUEST_CODE = 21
    const val FOREGROUND_REQUEST_CODE = 22
    const val LOCATION_PERMISSION_INDEX = 0
    const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
    const val REQUEST_ACCESS_FINE_LOCATION_CODE = 1590
}

fun isPermissionGranted(context : Context, permission: String): Boolean {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
        PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
            context,
            permission)
    } else {
        true
    }
}