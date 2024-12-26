package com.turskyi.malaknyzhka.models

sealed class NavigationDestination {
    data object Landing : NavigationDestination()
    data object Book : NavigationDestination()
}