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
import com.example.android_mvp.data.camera.CameraHandler
import com.example.android_mvp.data.camera.apiModel
import com.example.android_mvp.data.camera.toCorrectlyRotatedBitmap
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
    private val cameraHelper: CameraHandler
): ViewModel(){

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun takePhoto(
    ) {
        _uiState.update { it.copy(isLoading = true) }

        cameraHelper.takePhoto() { bitmap ->
            _uiState.update { it.copy(latestPhoto = bitmap) }

            viewModelScope.launch {
                try {
                    val result = classificationRepository.classify(bitmap, _uiState.value.isOnDevice)
                    _uiState.update { current ->
                        current.copy(
                            classificationText = result.label,
                            confidenceValue = result.confidence,
                            timeTakenDuration = result.timeMs,
                            isLoading = false
                        )
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun clearPhoto() {
        _uiState.update { MainUiState(latestPhoto = null) }
    }

    fun switchMode(isOnDevice: Boolean) {
        _uiState.update { it.copy(isOnDevice = !isOnDevice) }
    }
}