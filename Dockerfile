FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# The context should be the root of the project.
# We expect the server distribution to be pre-built in server/build/install/server
COPY server/build/install/server /app

# Expose the port Ktor will run on
EXPOSE 8080

# Run the server
CMD ["./bin/server"]
