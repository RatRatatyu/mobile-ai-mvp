package com.example.android_mvp.data.model

import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions


class MlKitImageLabeler{
     val options = ImageLabelerOptions.Builder()
     .setConfidenceThreshold(0.4f)
     .build()

     val labeler = ImageLabeling.getClient(options)


    fun analyze( bitmap: Bitmap, onResult: (String, Float, Long) -> Unit){
        val startTime = System.currentTimeMillis()
        val image = InputImage.fromBitmap(bitmap, 0)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val endTime = System.currentTimeMillis()
                val duration = endTime - startTime
                onResult(labels.firstOrNull()?.text ?: "null", labels.firstOrNull()?.confidence ?: 0f, duration)


            }
            .addOnFailureListener { e ->
                Log.e("error", "",e)


            }
    }

}


