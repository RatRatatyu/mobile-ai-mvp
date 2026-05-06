package com.example.android_mvp.core


interface MainContract {

    interface View {
        fun hasPermission(state: Boolean)

    }

    interface Presenter {
        fun checkCameraPermission()

    }
}