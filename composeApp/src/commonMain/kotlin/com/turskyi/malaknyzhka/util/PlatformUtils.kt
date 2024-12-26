package com.turskyi.malaknyzhka.util

import com.turskyi.malaknyzhka.getPlatform
import com.turskyi.malaknyzhka.models.PlatformType

fun isOnWeb(): Boolean {
    return getPlatform().type == PlatformType.WEB
}
