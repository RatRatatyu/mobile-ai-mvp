package com.example.android_mvp.data.repository

import android.graphics.Bitmap
import androidx.compose.runtime.rememberCoroutineScope
import com.example.android_mvp.data.camera.apiModel
import com.example.android_mvp.data.model.ApiModel
import com.example.android_mvp.data.model.MlKitImageLabeler
import kotlinx.coroutines.launch


data class ClassificationResult(
    val label: String,
    val confidence: Float,
    val timeMs: Long
)

class ClassificationRepository(
    private val mlKitLabel: MlKitImageLabeler = MlKitImageLabeler(),
    private val apiModel: ApiModel = ApiModel()
){

    suspend fun classify(
        bitmap: Bitmap,
        isOnDevice: Boolean,
    ): ClassificationResult {
        val startTime = System.currentTimeMillis()

        return if (isOnDevice) {
            mlKitLabel.analyze(bitmap) { label, confidence  ->
                val duration = System.currentTimeMillis() -  startTime
                ClassificationResult(label, confidence, duration)
            }
        } else {
            apiModel.classifyImage(bitmap) { label, confidence ->
                val duration = System.currentTimeMillis() -  startTime
                ClassificationResult(label, confidence, duration)
            }
        }
    }
}