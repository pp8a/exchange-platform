# exchange-platform

**Микросервисная система управления пользователями и переводами средств**

---

## 📦 Состав проекта

- `auth-service` — аутентификация по email/phone и паролю, генерация JWT.
- `currency-service` — управление аккаунтами, балансами, переводами и историей переводов.
- `exchange-gateway` — API Gateway (WebFlux), маршрутизация запросов, JWT-фильтрация.

---

## 🚀 Инструкции по запуску

```bash
# Клонируйте репозиторий
git clone https://github.com/your-username/exchange-platform.git
cd exchange-platform

# Запустите все сервисы и инфраструктуру
docker-compose up --build
```

### Swagger
Swagger UI доступен по адресу:
```
http://localhost:8081/swagger-ui.html
```

---

## 🔐 Аутентификация и пример использования

1. Получение токена:

```
POST http://localhost:8080/auth/token
{
  "identifier": "charlie@example.com",
  "password": "password3"
}
```

2. Использование токена в API-запросах (тип Bearer Token):

```
PUT http://localhost:8080/api/currency/account/transfer
{
  "toUserId": "00000000-0000-0000-0000-000000000001",
  "amount": 10.00
}
```

---

## ⚙️ Используемые технологии

- **Java 21 + Spring Boot 3 + WebFlux**
- **PostgreSQL + Liquibase** — миграции в `migration-service`
- **MongoDB + Mongock** — события и логи
- **Redis (Reactive)** — кэширование балансов
- **Kafka KRaft + Avro** — события `TransferEvent`, `UserEvent`
- **OpenAPI (SpringDoc 2.7.0)** — генерация документации
- **Testcontainers** — интеграционные тесты
- **Reactor Kafka** — Kafka consumer

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
- **Kafka**: Avro-сообщения отправляются через `UserKafkaProducer`, потребляются и логируются в MongoDB

---

## ✅ Логика валидаций и доступов

- Email/phone должен быть уникальным.
- Удаление и изменение возможны только владельцем.
- Все операции требуют валидного JWT (`X-USER-ID` прокидывается из Gateway).
