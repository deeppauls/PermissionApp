package com.example.permissionapp

import android.Manifest
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale


class MainActivity : AppCompatActivity() {

    private var cameraResultLauncher: ActivityResultLauncher<String> =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_SHORT).show()
            }
        }

    private var cameraAndLocationResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Permission granted for location", Toast.LENGTH_SHORT)
                            .show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission granted for coarse location", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Permission granted for camera", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    if (permissionName == Manifest.permission.ACCESS_FINE_LOCATION) {
                        Toast.makeText(this, "Permission denied for fine location", Toast.LENGTH_SHORT)
                            .show()
                    }else if(permissionName == Manifest.permission.ACCESS_COARSE_LOCATION){
                        Toast.makeText(this, "Permission denied for coarse location", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(this, "Permission denied for camera", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCamera: Button = findViewById(R.id.btnCameraPermission)
        btnCamera.setOnClickListener {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
                showRationalDialog("Permission demo requires camera access",
                    "Camera access is denied")
            }else{
                cameraAndLocationResultLauncher.launch(
                    arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
        }
    }

    private fun showRationalDialog(title: String, message: String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel"){dialog, _->
                dialog.dismiss()
            }
        builder.create().show()
    }
}



