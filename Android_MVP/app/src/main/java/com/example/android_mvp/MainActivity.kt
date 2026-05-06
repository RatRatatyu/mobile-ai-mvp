package com.example.android_mvp

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_mvp.core.MainContract
import com.example.android_mvp.core.MainPresenter
import com.example.android_mvp.ui.theme.Android_MVPTheme



class MainActivity : ComponentActivity(), MainContract.View {

    private lateinit var presenter: MainPresenter
    private var hasCameraPermission by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        presenter = MainPresenter(this, this)

        setContent {
            Android_MVPTheme {
                MainScreen(
                    hasCameraPermission,
                    onRequestCameraClick = { presenter.checkCameraPermission() }
                )
            }
        }
        presenter.checkCameraPermission()
    }

    override fun hasPermission(state: Boolean) {
        hasCameraPermission = state
    }




}


@Composable
fun MainScreen(
    hasPermission: Boolean,
    onRequestCameraClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),

        ) {
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ){
                if(hasPermission){
                    Text("hi", textAlign = TextAlign.Center)
                }else{
                    Button(onClick = onRequestCameraClick) {Text("Premmisom") }
                }
            }
        }
    }
}