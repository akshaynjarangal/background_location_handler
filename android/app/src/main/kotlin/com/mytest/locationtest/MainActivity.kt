package com.mytest.locationtest

import android.Manifest
import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
    private val CHANNEL = "com.akshay.location"
    private val PERMISSION_REQUEST_CODE = 123

    @RequiresApi(Build.VERSION_CODES.M)
    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            when (call.method) {
                "checkLocationPermission" -> {
                    val permissionStatus = checkLocationPermissionStatus()
                    result.success(permissionStatus)
                }
                "openLocationSettings" -> {
                    openAppLocationSettings()
                    result.success(null)
                }
                else -> result.notImplemented()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkLocationPermissionStatus(): String {
        return when {
            checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
                // Permission granted
                val appOps = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
                val mode = appOps.checkOpNoThrow(
                    AppOpsManager.OPSTR_FINE_LOCATION,
                    android.os.Process.myUid(),
                    packageName
                )
                when {
                    mode == AppOpsManager.MODE_ALLOWED -> "ALLOWED_ALL_TIME"
                    else -> "ALLOWED_ONLY_WHILE_IN_USE"
                }
            }
            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // Permission denied, but can be requested again
                "DENIED_BUT_REQUESTABLE"
            }
            else -> {
                // Permission denied and cannot be requested again
                "DENIED"
            }
        }
    }

    private fun openAppLocationSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivityForResult(intent, PERMISSION_REQUEST_CODE)
    }
}
