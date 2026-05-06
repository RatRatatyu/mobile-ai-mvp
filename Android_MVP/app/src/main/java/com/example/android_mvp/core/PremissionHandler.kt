package com.example.android_mvp.core

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainPresenter(
    private val view: MainContract.View,
    private val activity: ComponentActivity
) : MainContract.Presenter {


    private val requestPermissionLauncher = activity.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        view.hasPermission(isGranted)

    }

    override fun checkCameraPermission() {
        val permissionStatus = ContextCompat.checkSelfPermission(
            activity, Manifest.permission.CAMERA
        )

        when {
            // already have permission
            permissionStatus == PackageManager.PERMISSION_GRANTED -> {
                view.hasPermission(true)
            }

            // showing why we need permission if user decline earlier
            activity.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                 showRationaleAndRequest()
            }

            // first try or permanently denied
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun showRationaleAndRequest() {
        AlertDialog.Builder(activity)
            .setTitle("Нужен доступ к камере")
            .setMessage("Приложению нужна камера, чтобы делать фото и отправлять их на сервер для обработки.")
            .setPositiveButton("Продолжить") { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

}

