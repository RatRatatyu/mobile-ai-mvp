package com.example.android_mvp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.android_mvp.core.PermissionHandler
import com.example.android_mvp.data.MainScreen
import com.example.android_mvp.ui.theme.Android_MVPTheme

class MainActivity : ComponentActivity() {


    private val viewModelPermission: PermissionHandler by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModelPermission.onPermissionCheckResult(isGranted = isGranted)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val permissionState by viewModelPermission.uiState.collectAsStateWithLifecycle()

            Android_MVPTheme {
                MainScreen(
                    viewModel = viewModel(),
                    hasPermission = permissionState.hasPermission,
                    onRequestCameraClick = { checkCameraPermission() }
                )
                if (permissionState.showRationaleDialog) {
                    AlertDialog(
                        onDismissRequest = { viewModelPermission.setShowRationale(false) },
                        title = { Text("Нужен доступ к камере") },
                        text = { Text("Приложению нужна камера, чтобы делать фото и классифицировать из через модели.") },
                        confirmButton = {
                            Button(onClick = {
                                viewModelPermission.setShowRationale(false)
                                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                            }) {
                                Text("Продолжить")
                            }
                        },
                        dismissButton = {
                            Button(onClick = { viewModelPermission.setShowRationale(false) }) {
                                Text("Отмена")
                            }
                        }
                    )
                }
            }
        }

        checkCameraPermission()
    }

    private fun checkCameraPermission() {
        val permission = Manifest.permission.CAMERA
        val permissionStatus = ContextCompat.checkSelfPermission(this, permission)

        when {
            // already have permission
            permissionStatus == PackageManager.PERMISSION_GRANTED -> {
                viewModelPermission.onPermissionCheckResult(isGranted = true)
            }
            // showing why we need permission if user decline earlier
            ActivityCompat.shouldShowRequestPermissionRationale(this, permission) -> {
                viewModelPermission.setShowRationale(true)
            }
            // first try or permanently denied
            else -> {
                requestPermissionLauncher.launch(permission)
            }
        }
    }
}