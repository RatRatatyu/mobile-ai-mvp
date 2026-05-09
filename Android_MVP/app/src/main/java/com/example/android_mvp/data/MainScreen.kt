package com.example.android_mvp.data

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.android_mvp.R
import com.example.android_mvp.data.camera.CameraPreview
import com.example.android_mvp.data.camera.takePhoto


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    hasPermission: Boolean,
    onRequestCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var latestPhoto by remember { mutableStateOf<Bitmap?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember(latestPhoto == null) {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
        }
    }
    val scope = rememberCoroutineScope()
    var isOnDevice by remember { mutableStateOf(true) }

    var classificationText by remember { mutableStateOf("") }
    var confidenceValue by remember { mutableFloatStateOf(0f) }
    var timeTakenDuration by remember { mutableLongStateOf(0) }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                actions = {
                    Row (verticalAlignment = Alignment.CenterVertically){
                        Text(
                            "OnServer",
                            style = MaterialTheme.typography.bodySmall,)
                        Switch(
                            checked = isOnDevice,
                            onCheckedChange = { isOnDevice = !isOnDevice })
                        Text(
                            "OnDevice",
                            style = MaterialTheme.typography.bodySmall,)
                    }
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
                    if(latestPhoto == null) { //showing camera if image is null
                        CameraPreview(
                            cameraController, lifecycleOwner, modifier
                        )
                    }else{
                            Image( //showing image instead of camera
                                bitmap = latestPhoto!!.asImageBitmap(),
                                contentDescription = stringResource(R.string.photo_description),
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                    }
                } else { //showing button to get camera permission
                    Button(onClick = onRequestCameraClick) { Text(stringResource(R.string.ask_permission_button)) }
                }
            }
            ButtonsController(
                onTakePhotoClick = {
                    takePhoto(
                        cameraController = cameraController,
                        context = context,
                        onClassificationResult = { label, confidence, duration ->
                            timeTakenDuration = duration
                            classificationText = label
                            confidenceValue = confidence
                        },
                        isOnDevice = isOnDevice,
                        scope = scope,
                        onPhotoTaken = { bitmap ->
                            latestPhoto = bitmap
                        }
                    )
                },
                onClearPhotoClick = {
                    latestPhoto = null
                    classificationText = ""
                    confidenceValue = 0f

                },
                latestPhoto = latestPhoto,
                modifier = Modifier.weight(1f)
            )

            ModelResult(
                classificationText,
                confidenceValue,
                timeTakenDuration,
                modifier = Modifier.weight(1f)
            )

        }
    }
}


@Composable
fun ModelResult(
    classificationText: String,
    confidenceValue: Float,
    timeTakenDuration: Long,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 20.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.CenterStart
    ){

        Column {
            Text(
                text = "Объект: $classificationText",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Уверенность: ${(confidenceValue * 100).toInt()}%",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = "Время предсказания: $timeTakenDuration мс",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}
@Composable
fun ButtonsController(
    onTakePhotoClick: () -> Unit,
    onClearPhotoClick: () -> Unit,
    latestPhoto: Bitmap?,
    modifier: Modifier = Modifier,

) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        if(latestPhoto == null){
            IconButton(
                onClick = onTakePhotoClick,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.PhotoCamera,
                    contentDescription = "Сделать фото",
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }else{
            IconButton(
                onClick = onClearPhotoClick,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.Filled.RestartAlt,
                    contentDescription = "Сфоткать снова",
                    modifier = Modifier.size(42.dp),
                    tint = MaterialTheme.colorScheme.inversePrimary
                )
            }
        }

    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    MaterialTheme {
        MainScreen(
            true,
            {}
        )
    }

}
