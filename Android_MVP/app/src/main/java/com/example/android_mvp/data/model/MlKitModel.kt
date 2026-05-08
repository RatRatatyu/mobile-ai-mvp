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


    fun analyze( bitmap: Bitmap, onResult: (String, Float) -> Unit){
        val image = InputImage.fromBitmap(bitmap, 0)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                onResult(labels.firstOrNull()?.text ?: "null", labels.firstOrNull()?.confidence ?: 0f)


            }
            .addOnFailureListener { e ->
                Log.e("error", "",e)


            }
    }

}


