# 📦 Exchange Platform

**Микросервисная система управления пользователями и переводами средств**

---

## 📦 Состав проекта

- `currency-service` — микросервис для управления аккаунтами, балансами, денежными переводами и логированием в MongoDB через Kafka (WebFlux + PostgreSQL + Redis), миграция MongoDB (Mongock).
- `exchange-gateway` —  API Gateway на Spring WebFlux, отвечает за маршрутизацию запросов, валидацию JWT, аутентификацию по email/phone и генерацию токенов.
- `eureka-server` — сервер регистрации и обнаружения сервисов (Eureka).
- `migration-service` — сервис миграций для PostgreSQL (Liquibase).

---

## 🚀 Инструкции по запуску

```bash
# Клонируйте репозиторий
git clone https://github.com/your-username/exchange-platform.git
cd exchange-platform

# Запустите все сервисы и инфраструктуру
docker-compose up --build -d

# Доступные сервисы
- **Gateway**: http://localhost:8080  
  ⤷ Центральная точка входа в систему через Spring Cloud Gateway.  

- **Swagger UI (currency-service)**: http://localhost:8081/swagger-ui.html  
  ⤷ Документация REST API микросервиса управления аккаунтами и переводами.  

- **Kafka UI**: http://localhost:8088  
  ⤷ Графический интерфейс для просмотра Kafka-топиков, сообщений и консюмер-групп.  

- **Schema Registry**: http://localhost:8085  
  ⤷ Используется для хранения Avro-схем сообщений Kafka.  

- **PostgreSQL**: `jdbc:postgresql://localhost:5432/currency_db`  
  ⤷ Основная база данных для хранения аккаунтов и операций.  

- **MongoDB**: `mongodb://localhost:27017`  
  ⤷ Используется для хранения событий (event sourcing) — переводов и изменений баланса.  

- **Redis**: `redis://localhost:6379`  
  ⤷ Кэширование актуального баланса и ускорение запросов к аккаунтам.  
```
## 🛠 Используемые технологии

- **Java 21 + Spring Boot 3 + WebFlux**
- **PostgreSQL + R2DBC**
- **MongoDB + Mongock**
- **Redis (Reactive)** - кэширование баланса
- **Kafka (на KRaft) + Avro** — события `TransferEvent`, `UserEvent`
- **Liquibase** — миграции для PostgreSQL (в `migration-service`)
- **Mongock** — миграции MongoDB (`transfer_logs`, `currency_events`)
- **OpenAPI (springdoc-openapi)** — Swagger UI для API документации
---

## 🔐 Аутентификация и пример использования

1. Получение токена:

```
POST http://localhost:8080/auth/token
Content-Type: application/json
{
  "identifier": "charlie@example.com",
  "password": "password3"
}
```

2. Использование токена в API-запросах (тип Bearer Token):

```
PUT http://localhost:8080/api/currency/account/transfer
Authorization: Bearer {access_token}
{
  "toUserId": "00000000-0000-0000-0000-000000000001",
  "amount": 10.00
}
```

---

## 🧠 Реализация и архитектура

### Бизнес-логика

- Каждый пользователь имеет:
  - ≥1 email и phone
  - строго 1 `account` с `balance` и `initialBalance`
- Баланс не может уйти в минус.
- Раз в **30 секунд** баланс увеличивается на **10%**, но не более **207%** от начального.

### Кэширование

- Redis используется для хранения текущих балансов пользователей.
- API сначала обращается к кэшу, затем к PostgreSQL.

### Трансферы

- Авторизованный пользователь может отправить деньги другому.
- Операция потокобезопасна, банковского уровня:
  - проверка баланса
  - запись в PostgreSQL
  - публикация `TransferEvent` в Kafka
  - Kafka consumer логирует в MongoDB (`transfer_logs`)

---

## 🧪 Тестирование

- ✅ Unit-тесты для трансфера средств - currency-service
- ✅ Интеграционные тесты API с использованием **Testcontainers** - exchange-gateway
- ✅ Тестирование WebFlux с `WebTestClient` - exchange-gateway

---

## 📋 Swagger / OpenAPI

- Подключён через `springdoc-openapi-starter-webflux-ui` (v2.7.0)
- Аннотации `@Operation`, `@Parameter`, `@Tag` добавлены ко всем контроллерам

---

## 📊 Миграции и данные

- **PostgreSQL**: миграции через **Liquibase** (в `migration-service`)
  - `clients`, `account`, `email_data`, `phone_data`, `client_personal_data`
- **MongoDB**: миграции через **Mongock**
  - Коллекции: `transfer_logs`
- **Kafka**: Avro-сообщения отправляются через `**Producer`, потребляются и логируются в MongoDB

---

## ✅ Логика валидаций и доступов

- Email/phone должен быть уникальным.
- Удаление и изменение возможны только владельцем.
- Все операции требуют валидного JWT (`X-USER-ID` прокидывается из Gateway).
