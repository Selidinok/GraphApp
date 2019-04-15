package com.pointer.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.dsl.module.Module
import org.koin.standalone.StandAloneContext.loadKoinModules
import timber.log.Timber


abstract class BaseFragment : Fragment() {
    abstract val layoutRes: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.d("Fragment ${this::class.java.simpleName} created")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Timber.d("Fragment ${this::class.java.simpleName} view created")
        super.onCreate(savedInstanceState)
        return inflater.inflate(layoutRes, container, false)
    }

    open fun onBackPressed() {
        Timber.d("${this::class.java.simpleName} onBackPressed")
    }
}
