# Build stage
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .

# Copy server module
COPY server server
# We also need to copy common parts if server depends on them (e.g. libs.versions.toml)
# libs.versions.toml is inside gradle/ which we already copied.

# Build the server (installDist creates a runnable distribution)
# We use -PcomposeApp.skip=true or just build the specific task.
# Since settings.gradle.kts includes :composeApp, we should at least have the directory
# or Gradle might complain. Let's create an empty one just in case.
RUN mkdir composeApp

RUN ./gradlew :server:installDist --no-daemon

# Runtime stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the build output from the build stage
COPY --from=build /app/server/build/install/server /app

# Expose the port Ktor will run on
EXPOSE 8080

# Run the server
CMD ["./bin/server"]
