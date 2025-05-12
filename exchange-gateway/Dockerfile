# Используем образ JDK 17
FROM eclipse-temurin:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/exchange-gateway-*.jar exchange-gateway.jar

# Указываем порт, который будет использоваться внутри контейнера
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "exchange-gateway.jar"]
