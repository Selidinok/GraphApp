package com.example.form.presentation

import androidx.core.os.bundleOf
import androidx.core.text.isDigitsOnly
import com.example.form.domian.GetPointsUseCase
import com.pointer.core.base.AppExceptions
import com.pointer.core.base.BaseViewModel
import com.pointer.core.base.ScreenRouter
import com.pointer.core.consts.ScreenEnum
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FormViewModel(
    private val getPointsUseCase: GetPointsUseCase,
    private val router: ScreenRouter
) : BaseViewModel<ViewState, ViewEffect, ViewEvent>() {

    override fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.TextChange -> updateState(event.text)
            is ViewEvent.Request -> getPoints()
            is ViewEvent.BackPressed -> router.exit()
        }
    }

    private fun updateState(text: String) {
        if (text.isNotBlank() && text.isDigitsOnly()) _viewState.value = ViewState(text.toInt())
        else _viewEffect.value = ViewEffect.Error(AppExceptions.ValidationException())
    }

    private fun getPoints() {
        actionWithLoading {
            getPointsUseCase(viewState.value!!.count).fold(
                {
                    withContext(Dispatchers.Main) {
                        Timber.d("$it")
                        val screen = ScreenEnum.GRAPH.apply {
                            bundle = bundleOf(ScreenEnum.GRAPH.name to it.toTypedArray())
                        }
                        router.navigateTo(screen)
                    }
                },
                {
                    _viewEffect.postValue(ViewEffect.Error(it))
                }
            )

        }
    }

    private fun actionWithLoading(action: suspend () -> Unit) {
        _viewEffect.value = ViewEffect.Loading
        launch {
            action()
        }
    }
}