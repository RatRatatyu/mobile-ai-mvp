package com.example.android_mvp.data.camera

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.android_mvp.R

@Composable
fun CameraHelper(
    latestPhoto: Bitmap?,
    onTakePhoto: (Bitmap?) -> Unit,
    onCleanPhoto: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember() {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }

    fun onTakePhoto(
    ) {
        val executor = ContextCompat.getMainExecutor(context)

        cameraController.takePicture(
            executor,
            object : ImageCapture.OnImageCapturedCallback() {
                @OptIn(ExperimentalGetImage::class)
                override fun onCaptureSuccess(image: ImageProxy) {

                    val bitmap = image.toCorrectlyRotatedBitmap()
                    onTakePhoto(bitmap)
                    image.close()

                }

                override fun onError(exception: ImageCaptureException) {
                    Log.e("Camera", "camera error", exception)
                    onTakePhoto(null)

                }
            }
        )
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(all = 20.dp)
                .weight(3f)
                .clip(RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ){
            if(latestPhoto == null){
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
            }else{
                Image(
                    bitmap = latestPhoto.asImageBitmap(),
                    contentDescription = stringResource(R.string.photo_description),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            if(latestPhoto == null){
                IconButtonHelper(
                    { onTakePhoto },
                    Icons.Filled.PhotoCamera,
                    "Сделать фото"
                )
            }else{
                IconButtonHelper(
                    { onCleanPhoto },
                    Icons.Filled.RestartAlt,
                    "Сфоткать снова"
                )
            }

        }

    }
}

@Composable
fun IconButtonHelper(
    onClickFun: () -> Unit,
    icon: ImageVector,
    contectDescriptionText: String,
) {

    IconButton(
        onClick = onClickFun,
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contectDescriptionText,
            modifier = Modifier.size(42.dp),
            tint = MaterialTheme.colorScheme.inversePrimary
        )
    }

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


