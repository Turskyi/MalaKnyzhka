package com.turskyi.malaknyzhka.router

sealed class NavigationDestination {
    data object Landing : NavigationDestination()
    data object Book : NavigationDestination()
}