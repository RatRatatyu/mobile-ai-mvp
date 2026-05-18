package com.example.android_mvp.presentation

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_mvp.data.model.ApiModel
import com.example.android_mvp.data.model.MlKitImageLabeler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel(){

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()
    val mlKit = MlKitImageLabeler()
    val apiModel = ApiModel()

    fun onTakePhoto(image: Bitmap?){
        _uiState.update { it.copy(latestPhoto = image) }
        classifyImage(image)
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


    private fun classifyImage(bitmap: Bitmap?) {
        if (bitmap == null) return
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            val startTime = System.currentTimeMillis()

            try {
                val (label, confidence) = if (_uiState.value.isOnDevice) {
                    mlKit.analyze(bitmap!!)
                } else {
                    apiModel.classifyImage(bitmap!!)
                }

                val duration = System.currentTimeMillis() - startTime

                _uiState.update {
                    it.copy(
                        classificationText = label,
                        confidenceValue = confidence,
                        timeTakenDuration = duration,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        classificationText = "Ошибка",
                        confidenceValue = 0f,
                        timeTakenDuration = 0L
                    )
                }
            }
        }
    }
}