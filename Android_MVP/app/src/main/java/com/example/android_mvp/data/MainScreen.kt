package com.example.android_mvp.data

import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.example.android_mvp.R
import com.example.android_mvp.data.camera.CameraPreview
import com.example.android_mvp.data.camera.takePhoto
import com.example.android_mvp.ui.theme.Android_MVPTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    hasPermission: Boolean,
    onRequestCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isImageTaken by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
            bindToLifecycle(lifecycleOwner)
        }
    }


    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.app_name),
                        style = MaterialTheme.typography.titleLarge)
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

            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 20.dp)
                    .weight(2f)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ){
                if(hasPermission){
                    CameraPreview(
                        context, lifecycleOwner , cameraController, modifier
                    )
                }else{
                    Button(onClick = onRequestCameraClick) {Text(stringResource(R.string.ask_permission_button)) }
                }
            }
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 20.dp)
                    .weight(2f),
                contentAlignment = Alignment.Center
            ){
                Button(onClick = { takePhoto(cameraController, context, {  }) }) {
                    Text("hi")
                }
                isImageTaken?.let { uri ->
                    Box(modifier = Modifier.padding(16.dp)) {
                        Image(
                            painter = rememberAsyncImagePainter(uri),
                            contentDescription = "Сделанное фото",
                            modifier = Modifier
                                .size(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )
                    }

                }

            }


        }
    }
}

@Preview
@Composable
private fun PreviewMainScree() {
    Android_MVPTheme{
        MainScreen(
            false,
            {print("hi")}
        )
    }
}