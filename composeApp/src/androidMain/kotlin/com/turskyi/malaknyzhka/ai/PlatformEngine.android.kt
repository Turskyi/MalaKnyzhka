package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.android.Android

actual fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient = HttpClient(Android) {
    config()
}
