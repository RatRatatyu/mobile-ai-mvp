package com.example.android_mvp.presentation

import android.graphics.Bitmap

data class MainUiState(
    val isLoading: Boolean? = null,
    val latestPhoto: Bitmap? = null,
    val isOnDevice: Boolean = true,
    val classificationText: String = "",
    val confidenceValue: Float = 0f,
    val timeTakenDuration: Long = 0,

)

