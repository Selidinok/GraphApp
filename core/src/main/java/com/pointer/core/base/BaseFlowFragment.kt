package com.pointer.core.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.pointer.core.R
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.bindScope
import org.koin.androidx.scope.ext.android.createScope
import org.koin.core.scope.ScopeCallback
import org.koin.dsl.module.Module
import org.koin.standalone.StandAloneContext
import org.koin.standalone.StandAloneContext.loadKoinModules
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import timber.log.Timber

abstract class BaseFlowFragment : BaseFragment() {

    abstract val containerId: Int
    private val navigatorHolder: NavigatorHolder by inject()
    private val currentFragment: BaseFragment?
        get() = childFragmentManager.findFragmentById(containerId) as? BaseFragment

    protected val router: ScreenRouter by inject()
    protected val navigator: Navigator by lazy {
        object : SupportAppNavigator(this.activity, childFragmentManager, containerId) {

            override fun activityBack() {
                super.activityBack()
            }

            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                //fix incorrect order lifecycle callback of MainFlowFragment
                fragmentTransaction.setReorderingAllowed(true)
                if (currentFragment != null
                    && nextFragment != null && currentFragment::class.java != nextFragment::class.java
                ) {
                    fragmentTransaction.setCustomAnimations(R.animator.slide_up, R.animator.slide_down, R.animator.slide_up, R.animator.slide_down)
                }
            }
        }
    }



    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        navigatorHolder.setNavigator(navigator)
    }

    override fun onStop() {
        Timber.d("onStop")
        navigatorHolder.removeNavigator()
        super.onStop()
    }

    open fun onExit() {}

    override fun onBackPressed() {
        Timber.d("${this::class.java.simpleName} onBackPressed")
        currentFragment?.onBackPressed() ?: super.onBackPressed()
    }
}