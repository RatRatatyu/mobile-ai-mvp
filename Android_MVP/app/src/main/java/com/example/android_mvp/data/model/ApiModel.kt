package com.example.android_mvp.data.model
import com.example.android_mvp.BuildConfig
import android.graphics.Bitmap
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Url
import java.io.ByteArrayOutputStream


const val token = BuildConfig.HF_TOKEN


data class PredictionResponse(val label: String, val score: Float)

interface HuggingFaceApi {

    @POST
    suspend fun postImage(
        @Url url: String,
        @Header("Authorization") token: String,
        @Body body: RequestBody
    ): List<PredictionResponse>


}

class ApiModel(){
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://router.huggingface.co/hf-inference/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();

    val service: HuggingFaceApi = retrofit.create(HuggingFaceApi::class.java)

     suspend fun postI(imageBitmap: Bitmap, onResult: (String, Float) -> Unit){
        val imageToButeArray = compressBitmap((imageBitmap))
        val requestBody = imageToButeArray.toRequestBody("image/jpeg".toMediaTypeOrNull())
        try {
            Log.d("TOKEN", token)
            val result = service.postImage(
                "models/google/vit-base-patch16-224",
                "Bearer $token",
                requestBody
            )
            onResult(result.firstOrNull()?.label ?: "null", result.firstOrNull()?.score ?: 0f)



        }catch (e: Exception){
            Log.e("network", "Request failed", e);

        }
    }





}






















// function to compress image to byte array
// we do that because passing image in bitmap state thought
// internet will be very heavy and too long witch not good for UX
suspend fun compressBitmap(bitmap: Bitmap): ByteArray = withContext(Dispatchers.IO) {

    val stream = ByteArrayOutputStream()

    bitmap.compress(
        Bitmap.CompressFormat.JPEG,
        80,
        stream
    )

     stream.toByteArray()
}