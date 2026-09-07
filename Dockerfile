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

# Remove composeApp from settings.gradle.kts to avoid configuration failure since it's not in the context
RUN sed -i '/include(":composeApp")/d' settings.gradle.kts

# Make gradlew executable
RUN chmod +x gradlew

# Build the server (installDist creates a runnable distribution)
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
