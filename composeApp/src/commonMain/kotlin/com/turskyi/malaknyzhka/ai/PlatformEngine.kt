package com.turskyi.malaknyzhka.ai

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig

expect fun createHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient
