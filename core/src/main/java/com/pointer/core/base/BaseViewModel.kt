package com.pointer.core.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.standalone.KoinComponent
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel<State, Effect, Event> : ViewModel(), CoroutineScope, KoinComponent {

    protected val job = Job()
    protected val _viewState = MutableLiveData<State>()
    protected val _viewEffect = SingleLiveData<Effect>()
    val viewState: LiveData<State>
        get() = _viewState
    val viewEffect: LiveData<Effect?>
        get() = _viewEffect

    abstract fun onEvent(event: Event)

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    override fun onCleared() {
        super.onCleared()
        Timber.d("ViewModel cancel")
        job.cancel()
    }
}