package com.example.android_mvp.data

import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.android_mvp.R
import com.example.android_mvp.data.camera.CameraPreview
import com.example.android_mvp.data.camera.takePhoto
import com.example.android_mvp.data.camera.toCorrectlyRotatedBitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    hasPermission: Boolean,
    onRequestCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var latestPhoto by remember { mutableStateOf<ImageProxy?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            bindToLifecycle(lifecycleOwner)
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 20.dp)
                    .weight(2f)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (hasPermission) {
                    if(latestPhoto == null) {
                        CameraPreview(
                            cameraController, modifier
                        )
                    }else{
                        latestPhoto?.let { imageProxy ->
                            val bitmap = imageProxy.toCorrectlyRotatedBitmap()

                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Сделанное фото",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                        }
                    }
                } else {
                    Button(onClick = onRequestCameraClick) { Text(stringResource(R.string.ask_permission_button)) }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 20.dp)
                    .weight(2f),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        takePhoto(cameraController, context) { photo ->
                            latestPhoto = photo
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("📸 Сфоткать")
                }



            }
        }
    }
}


