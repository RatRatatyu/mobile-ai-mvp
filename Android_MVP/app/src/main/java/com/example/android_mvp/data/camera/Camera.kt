package com.example.android_mvp.data.camera

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.android_mvp.R
import com.example.android_mvp.data.model.MlKitImageLabeler


@Composable
fun CameraPreview(
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    modifier: Modifier
) {



    AndroidView(
        modifier = modifier.fillMaxSize(),
        factory = {ctx->
            PreviewView(ctx).apply {
                this.controller = cameraController
            }
        },
        update = {
            cameraController.bindToLifecycle(lifecycleOwner)
        }
    )
}


fun takePhoto(
    cameraController: LifecycleCameraController,
    context: Context,
    onClassificationResult: (String, Float) -> Unit,
    onPhotoTaken: (Bitmap?) -> Unit     //  returning ImageProxy
) {
    val executor = ContextCompat.getMainExecutor(context)

    cameraController.takePicture(
        executor,
        object : ImageCapture.OnImageCapturedCallback() {
            @OptIn(ExperimentalGetImage::class)
            override fun onCaptureSuccess(image: ImageProxy) {

                val bitmap = image.toCorrectlyRotatedBitmap()
                onPhotoTaken(bitmap)

                MlKitImageLabeler().analyze(bitmap,) { label, confidence ->
                    onClassificationResult(label, confidence)   //  callback
                }
                image.close()

            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("Camera", context.getString(R.string.error_photo), exception)
                onPhotoTaken(null)
            }
        }
    )
}

fun ImageProxy.toCorrectlyRotatedBitmap(): Bitmap {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)

    val originalBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

    val matrix = Matrix().apply {
        postRotate(imageInfo.rotationDegrees.toFloat())
    }

    return Bitmap.createBitmap(
        originalBitmap,
        0,
        0,
        originalBitmap.width,
        originalBitmap.height,
        matrix,
        true
    )
}
