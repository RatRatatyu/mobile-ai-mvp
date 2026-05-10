package com.example.android_mvp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import com.example.android_mvp.core.MainContract
import com.example.android_mvp.core.MainPresenter
import com.example.android_mvp.data.MainScreen
import com.example.android_mvp.presentation.MainViewModel
import com.example.android_mvp.ui.theme.Android_MVPTheme


private val MainActivity.MainViewModelFactory: ViewModelProvider.Factory

class MainActivity : ComponentActivity(), MainContract.View {

    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(classificationRepository, cameraHelper)
    }
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

