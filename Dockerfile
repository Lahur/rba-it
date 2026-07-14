# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM eclipse-temurin:25-jdk-jammy AS build
WORKDIR /workspace

COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew --no-daemon dependencies > /dev/null

COPY src ./src
RUN ./gradlew --no-daemon bootJar -x test

# ---- Runtime stage ----
FROM eclipse-temurin:25-jre-jammy AS runtime
WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring \
    && mkdir -p /app/data && chown -R spring:spring /app
USER spring

COPY --from=build --chown=spring:spring /workspace/build/libs/*.jar app.jar

VOLUME ["/app/data"]
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]