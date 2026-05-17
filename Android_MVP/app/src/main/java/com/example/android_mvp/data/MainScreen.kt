package com.example.android_mvp.data

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.android_mvp.R
import com.example.android_mvp.data.camera.CameraHelper
import com.example.android_mvp.presentation.MainViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    hasPermission: Boolean,
    onRequestCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val uiStateModel  by viewModel.uiState.collectAsStateWithLifecycle()

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
                            checked = uiStateModel.isOnDevice,
                            onCheckedChange = { viewModel.switchMode()})
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
                    .weight(3f),
                contentAlignment = Alignment.Center
            ) {
                if (hasPermission) {
                    CameraHelper(
                        uiStateModel.latestPhoto,
                        onTakePhoto = { bitmap ->
                            viewModel.onTakePhoto(bitmap)
                        },
                        onClearPhoto = {
                            viewModel.onClearPhoto()
                        },
                        modifier = Modifier)
                } else { //showing button to get camera permission
                    Button(onClick = onRequestCameraClick) { Text(stringResource(R.string.ask_permission_button)) }
                }
            }

            ModelResult(
                uiStateModel.classificationText,
                uiStateModel.confidenceValue,
                uiStateModel.timeTakenDuration,
                uiStateModel.isLoading,
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
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(all = 20.dp)
            .clip(RoundedCornerShape(12.dp)),
        contentAlignment = if (isLoading) Alignment.Center else Alignment.CenterStart
    ){

        Column {
            if(isLoading){
                CircularProgressIndicator()
            }else{
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
}

