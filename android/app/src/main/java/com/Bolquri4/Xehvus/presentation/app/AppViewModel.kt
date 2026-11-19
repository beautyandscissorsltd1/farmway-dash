package com.Bolquri4.Xehvus.presentation.app

import androidx.lifecycle.ViewModel
import com.Bolquri4.Xehvus.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val backStack = mutableListOf<Screen>()
    private var sessionCounter = 0L

    fun navigate(target: Screen, replace: Boolean = false) {
        val current = _screen.value
        if (!replace && current !is Screen.Splash) backStack.add(current)
        _screen.value = target
    }

    fun openGame(stageIndex: Int, replace: Boolean = false) {
        sessionCounter += 1
        navigate(Screen.Game(stageIndex, sessionCounter), replace)
    }

    fun toMenu() {
        backStack.clear()
        _screen.value = Screen.Menu
    }

    fun back() {
        val previous = backStack.removeLastOrNull()
        if (previous == null) {
            _screen.value = Screen.Menu
        } else {
            _screen.value = previous
        }
    }
}
