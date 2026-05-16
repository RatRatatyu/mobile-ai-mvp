package com.example.android_mvp.presentation

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.android_mvp.R
import com.example.android_mvp.data.model.MlKitImageLabeler
import com.example.android_mvp.data.repository.ClassificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val classificationRepository: ClassificationRepository,
): ViewModel(){

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun onTakePhoto(image: Bitmap){
        _uiState.update { it.copy(latestPhoto = image) }
    }

    fun onClearPhoto() {
        _uiState.update {
            it.copy(
                latestPhoto = null,
                timeTakenDuration = 0,
                classificationText = "",
                confidenceValue = 0f,
            )
        }
    }

    fun switchMode() {
        _uiState.update { it.copy(isOnDevice = !it.isOnDevice) }
    }
}