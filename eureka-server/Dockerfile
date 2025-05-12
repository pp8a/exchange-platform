# Используем образ JDK 17
FROM eclipse-temurin:21-jdk

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR файл в контейнер
COPY target/eureka-server-*.jar eureka-server.jar

# Указываем порт, который будет использоваться внутри контейнера
EXPOSE 8761

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "eureka-server.jar"]
