package com.turskyi.malaknyzhka.ai

import io.ktor.client.engine.HttpClientEngineFactory

expect fun getHttpClientEngine(): HttpClientEngineFactory<*>
