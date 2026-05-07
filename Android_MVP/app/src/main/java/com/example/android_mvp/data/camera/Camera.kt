package com.example.android_mvp.data.camera

import android.content.Context
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner


@Composable
fun CameraPreview(
    context: Context,
    lifecycleOwner: LifecycleOwner,
    cameraController: CameraController,

    modifier: Modifier
) {

    val executor = ContextCompat.getMainExecutor(context)

    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {ctx->
            PreviewView(ctx).apply {
                this.controller = cameraController
            }
        },
    )
}


fun takePhoto(
    controller: LifecycleCameraController,
    context: Context,
    onPhotoCaptured: (ImageProxy) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context), // Выполняем в основном потоке для простоты колбэка
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                onPhotoCaptured(image)
                print("taken")

            }

            override fun onError(exception: ImageCaptureException) {
                // Тут обрабатываем ошибку, если, например, камера занята
            }
        }
    )
}

