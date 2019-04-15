package com.pointer.core.base

import com.pointer.core.consts.ScreenEnum
import ru.terrakok.cicerone.android.support.SupportAppScreen

interface ScreenRouter {
    fun navigateTo(screen: ScreenEnum)

    fun newRootScreen(screen: ScreenEnum)

    fun replaceScreen(screen: ScreenEnum)

    fun backTo(screen: ScreenEnum)

    fun newChain(vararg screens: ScreenEnum)

    fun newRootChain(vararg screens: ScreenEnum)

    fun exit()

    fun getScreen(screen: ScreenEnum): SupportAppScreen
}