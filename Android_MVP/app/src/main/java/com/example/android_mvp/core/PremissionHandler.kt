package com.example.android_mvp.core

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class PermissionState(
    val hasPermission: Boolean = false,
    val showRationaleDialog: Boolean = false
)

class PermissionHandler : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionState())
    val uiState: StateFlow<PermissionState> = _uiState.asStateFlow()

    fun onPermissionCheckResult(isGranted: Boolean) {
        _uiState.update {
            it.copy(hasPermission = isGranted)
        }
    }

    fun setShowRationale(show: Boolean) {
        _uiState.update {
            it.copy(showRationaleDialog = show)
        }
    }
}