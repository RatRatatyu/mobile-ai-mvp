package com.example.android_mvp.data.model

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class MlKitImageLabeler {

    private val options = ImageLabelerOptions.Builder()
        .setConfidenceThreshold(0.4f)
        .build()

    private val labeler = ImageLabeling.getClient(options)

    suspend fun analyze(bitmap: Bitmap): Pair<String, Float> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()

            val firstLabel = labels.firstOrNull()
                ?: return "Не распознано" to 0f

            firstLabel.text to firstLabel.confidence

        } catch (e: Exception) {
            Log.e("MLKit", "Analyze error", e)
            "Ошибка ML Kit" to 0f
        }
    }
}