package com.example.graphapp.navigation

import android.os.Bundle
import com.pointer.core.base.BaseFragment
import com.pointer.core.base.ScreenRouter
import com.pointer.core.consts.ScreenEnum
import com.example.form.presentation.FormFragment
import com.example.graph.presenter.GraphFragment
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppScreen

class ScreenRouterImpl : Router(), ScreenRouter {

    override fun navigateTo(screen: ScreenEnum) {
        val appScreen = getScreen(screen)
        super.navigateTo(appScreen)
    }

    override fun newRootScreen(screen: ScreenEnum) {
        val appScreen = getScreen(screen)
        super.newRootScreen(appScreen)
    }

    override fun replaceScreen(screen: ScreenEnum) {
        val appScreen = getScreen(screen)
        super.replaceScreen(appScreen)
    }

    override fun backTo(screen: ScreenEnum) {
        val appScreen = getScreen(screen)
        super.backTo(appScreen)
    }

    override fun newChain(vararg screens: ScreenEnum) {
        val appScreens = screens.map { getScreen(it) }
        super.newChain(*appScreens.toTypedArray())
    }

    override fun newRootChain(vararg screens: ScreenEnum) {
        val appScreens = screens.map { getScreen(it) }
        super.newRootChain(*appScreens.toTypedArray())
    }

    override fun getScreen(screen: ScreenEnum): SupportAppScreen = when (screen) {
        ScreenEnum.FORM -> createScreen<FormFragment>(screen.bundle)
        ScreenEnum.GRAPH -> createScreen<GraphFragment>(screen.bundle)
    }

    override fun exit() {
        super.exit()
    }

    private inline fun <reified T : BaseFragment> createScreen(bundle: Bundle? = null) =
        object : SupportAppScreen() {
            override fun getFragment(): BaseFragment {
                return T::class.java.newInstance().apply {
                    bundle?.let { arguments = bundle }
                }
            }
        }
}