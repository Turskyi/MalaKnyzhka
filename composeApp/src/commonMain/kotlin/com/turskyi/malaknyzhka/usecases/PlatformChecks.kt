package com.turskyi.malaknyzhka.usecases

import com.turskyi.malaknyzhka.getPlatform
import com.turskyi.malaknyzhka.models.PlatformType

fun isOnWeb(): Boolean {
    return getPlatform().type == PlatformType.WEB
}

fun isOnAndroid(): Boolean {
    return getPlatform().type == PlatformType.ANDROID
}

fun isOnIos(): Boolean {
    return getPlatform().type == PlatformType.IOS
}

fun isOnDesktop(): Boolean {
    return getPlatform().type == PlatformType.DESKTOP
}
