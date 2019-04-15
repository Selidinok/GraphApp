package com.example.graphapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.pointer.core.base.BaseFlowFragment
import com.pointer.core.base.ScreenRouter
import com.pointer.core.consts.ScreenEnum
import org.koin.android.ext.android.inject
import ru.terrakok.cicerone.Navigator
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator
import ru.terrakok.cicerone.commands.Command
import timber.log.Timber

class AppMainActivity : AppCompatActivity() {

    private val router: ScreenRouter by inject()

    private val navigator: Navigator by lazy {
        object : SupportAppNavigator(this, supportFragmentManager, R.id.container) {
            override fun setupFragmentTransaction(
                command: Command?,
                currentFragment: Fragment?,
                nextFragment: Fragment?,
                fragmentTransaction: FragmentTransaction
            ) {
                fragmentTransaction.setReorderingAllowed(true)
            }
        }
    }
    private val navigatorHolder: NavigatorHolder by inject()

    private val currentFlowFragment: BaseFlowFragment?
        get() = supportFragmentManager.findFragmentById(R.id.container) as? BaseFlowFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            router.newRootScreen(ScreenEnum.FORM)
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        Timber.d("onResumeFragments")
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        Timber.d("Activity onPause")
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        Timber.d("${this::class.java.simpleName} onBackPressed")
        currentFlowFragment?.onBackPressed() ?: super.onBackPressed()
    }
}
