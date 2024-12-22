package com.turskyi.malaknyzhka

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform