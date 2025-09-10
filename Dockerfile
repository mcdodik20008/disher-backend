# ====== build stage ======
FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon

# ====== run stage ======
FROM eclipse-temurin:21-jre
ENV TZ=UTC \
    JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -XX:InitialRAMPercentage=50"
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar /app/app.jar --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-default}"]
