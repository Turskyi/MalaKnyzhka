FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# The context should be the root of the project.
# We expect the server distribution to be pre-built in server/build/install/server
COPY server/build/install/server /app

# Fix potential line ending issues in the start script and ensure it's executable
RUN sed -i 's/\r$//' /app/bin/server && chmod +x /app/bin/server

# Expose the port Ktor will run on
EXPOSE 8080

# Use the absolute path to the script to avoid ambiguity
ENTRYPOINT ["/app/bin/server"]
