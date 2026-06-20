plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.ktor)
}

group = "com.turskyi.malaknyzhka"
version = "1.0.0"

application {
    mainClass = "com.turskyi.malaknyzhka.ApplicationKt"
}

dependencies {
    implementation(libs.logback)
    implementation(libs.ktorServerCore)
    implementation(libs.ktorServerNetty)
    implementation(libs.ktorServerContentNegotiation)
    implementation(libs.ktorServerCors)
    implementation(libs.ktorClientCore)
    implementation(libs.ktorClientCio)
    implementation(libs.ktorClientContentNegotiation)
    implementation(libs.ktorSerializationKotlinxJson)
    implementation(libs.kotlinxSerializationJson)
    implementation(libs.dotenvKotlin)

    testImplementation(libs.ktorServerTestHost)
    testImplementation(libs.kotlinTestJunit)
}
