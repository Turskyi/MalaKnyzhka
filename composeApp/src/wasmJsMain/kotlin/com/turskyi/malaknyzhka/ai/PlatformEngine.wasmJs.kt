package com.turskyi.malaknyzhka.ai

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.js.Js

actual fun getHttpClientEngine(): HttpClientEngineFactory<*> = Js
