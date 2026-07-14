# Compose Desktop (release) ProGuard rules
-keep class io.ktor.serialization.kotlinx.** { *; }
-keep class io.ktor.serialization.kotlinx.json.** { *; }

# Ktor may reflectively access some bits
-dontwarn io.ktor.**

# Duplicate classes conflict resolution
-dontnote **
-dontwarn **
