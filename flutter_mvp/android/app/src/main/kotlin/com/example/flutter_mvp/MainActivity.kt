package com.example.flutter_mvp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import androidx.core.graphics.scale

class MainActivity: FlutterActivity() {
    private val labeler by lazy {
        val options = ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.4f)
            .build()

        ImageLabeling.getClient(options)
    }

    private val CHANNEL = "mlkit_photo_analyze"

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, CHANNEL).setMethodCallHandler { call, result ->
            if(call.method == "imageLabeling"){
                val imagePath = call.argument<String>("imagePath")
                if (imagePath == null) {
                    result.error("ArgError", "Image path is null", null)
                    return@setMethodCallHandler
                }
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                          val bitmap = BitmapFactory.decodeFile(imagePath)
                          val resizedBitmap = bitmap.scale(224, 224)
                        Log.d("MLKIT", "Before process")
                          val prediction = analyze(bitmap = resizedBitmap, labeler = labeler)

                        val response = mapOf(
                            "label" to prediction.label,
                            "confidence" to prediction.confidence
                        )

                        withContext(Dispatchers.Main){
                            result.success(response)
                        }
                    }catch (e: Exception){
                        withContext(Dispatchers.Main){
                            Log.e("MLKIT", "Analyze crash", e)
                            result.error(
                                "Error", e.message, null
                            )
                        }
                    }
                }
            } else {
                result.notImplemented()
            }

        }
    }
}

suspend fun analyze(bitmap: Bitmap, labeler: com.google.mlkit.vision.label.ImageLabeler): Prediction {

    val image = InputImage.fromBitmap(bitmap, 0)

    return try {
        val labels = labeler.process(image).await()
        Log.d("MLKIT", "Labels size = ${labels.size}")
        Log.d("MLKIT", "After process")
        Prediction(
            label = labels.firstOrNull()?.text ?: "null",
            confidence = labels.firstOrNull()?.confidence ?: 0f
        )

    } catch (e: Exception) {
        Log.e("error", "", e)
        Prediction(
            label = "error",
            confidence = 0f
        )
    }

}

data class Prediction(
    val label: String,
    val confidence: Float
)