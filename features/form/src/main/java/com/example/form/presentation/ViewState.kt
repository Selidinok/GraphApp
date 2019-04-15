package com.example.form.presentation

import com.pointer.core.base.AppExceptions

data class ViewState(
    val count: Int = 0
)

sealed class ViewEffect {
    object Loading : ViewEffect()
    data class Error(val error: AppExceptions) : ViewEffect()
}

sealed class ViewEvent {
    object BackPressed : ViewEvent()
    object Request : ViewEvent()
    data class TextChange(val text: String) : ViewEvent()
}

