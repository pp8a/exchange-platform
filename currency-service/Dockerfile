FROM eclipse-temurin:21-jdk

RUN apt update && apt install -y curl netcat-openbsd

WORKDIR /app

COPY target/currency-service-*.jar currency-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "currency-service.jar"]