package com.turskyi.malaknyzhka.util

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
